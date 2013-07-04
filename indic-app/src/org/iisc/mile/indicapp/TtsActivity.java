package org.iisc.mile.indicapp;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TtsActivity extends Activity {
	Button synthesizeButton;
	Button playButton;
	EditText inputText;

	private static final int CONNECTION_TIMEOUT = 5000; // in milliseconds
	private static final int SOCKET_TIMEOUT = 5000; // in milliseconds

	private static final String MILE_TTS_SERVER_URL = "http://mile.ee.iisc.ernet.in:8080";
	private static final String MILE_TTS_SERVICE_URL = MILE_TTS_SERVER_URL + "/tts_demo/rest";
	private static final String HTTP_SUCCESS_STATUS_MESSAGE = "HTTP/1.0 200 OK";

	String ttsInputText;
	String synthesizedSpeechFileUrl;
	ByteArrayOutputStream synthesizedSpeechByteStream = new ByteArrayOutputStream();
	boolean stopSynthesizedSpeech;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tts);

		synthesizeButton = (Button) findViewById(R.id.buttonSynthesize);
		playButton = (Button) findViewById(R.id.buttonPlay);
		playButton.setEnabled(false);
		inputText = (EditText) findViewById(R.id.textEditor);

		synthesizeButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Log.i("synthesizeButton.onClick()", "Synthesize button pressed");
				ttsInputText = inputText.getText().toString();
				playButton.setEnabled(false);
				new PostTextToServerAsync().execute();
			}
		});

		playButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if ("Play".equals(playButton.getText())) {
					new PlaySynthesizedSpeech().start();
					playButton.setText("Stop");
					stopSynthesizedSpeech = false;
				} else {
					stopSynthesizedSpeech = true;
					playButton.setText("Play");
				}
			}
		});

	}

	private class PostTextToServerAsync extends AsyncTask<URL, Void, String> {
		String httpPostStatus;

		@Override
		protected String doInBackground(URL... arg0) {
			Log.i("PostTextToServerAsync.doInBackground", "Before executing HTTP post");
			HttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters, CONNECTION_TIMEOUT);
			HttpConnectionParams.setSoTimeout(httpParameters, SOCKET_TIMEOUT);
			DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
			HttpPost httpPost = new HttpPost(MILE_TTS_SERVICE_URL);
			try {
				httpPost.setEntity(new StringEntity(ttsInputText, "UTF-8"));
				HttpResponse response = httpClient.execute(httpPost);
				Log.i("PostTextToServerAsync.doInBackground", "After executing HTTP post");
				httpPostStatus = response.getStatusLine().toString();
				if (response.getEntity() != null) {
					InputStream instream = response.getEntity().getContent();
					return convertStreamToString(instream);
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// This gets called on the main thread!
			if (result == null) {
				Toast.makeText(
						getApplicationContext(),
						"Time out in contacting MILE TTS service!\n"
								+ "Please check your internet connection.", Toast.LENGTH_LONG).show();
				return;
			} else {
				if (httpPostStatus.contains(HTTP_SUCCESS_STATUS_MESSAGE)) {
					synthesizedSpeechFileUrl = MILE_TTS_SERVER_URL
							+ result.substring(result.indexOf("<a href") + 11, result.indexOf(">here") - 1);
					Toast.makeText(getApplicationContext(), "Speech synthesis successful", Toast.LENGTH_LONG)
							.show();
					new GetSynthesizedFileAsync().execute();
				} else {
					Toast.makeText(getApplicationContext(), "Speech synthesis failed! Please try again.",
							Toast.LENGTH_LONG).show();
				}
			}
		}
	}

	private class GetSynthesizedFileAsync extends AsyncTask<URL, Void, String> {
		@Override
		protected String doInBackground(URL... arg0) {
			HttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters, CONNECTION_TIMEOUT);
			HttpConnectionParams.setSoTimeout(httpParameters, SOCKET_TIMEOUT);
			DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);
			HttpGet httpGet = new HttpGet(synthesizedSpeechFileUrl);
			try {
				HttpResponse response = httpclient.execute(httpGet);
				HttpEntity entity = response.getEntity();
				if (response.getEntity() != null) {
					InputStream inputStream = response.getEntity().getContent();
					synthesizedSpeechByteStream = createByteArrayOutputStream(inputStream);
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return "Success";
		}

		@Override
		protected void onPostExecute(String result) {
			if (result == null) {
				Toast.makeText(
						getApplicationContext(),
						"Time Out while getting synthesized speech!\n"
								+ "Please check your internet connection.", Toast.LENGTH_LONG).show();
				finish();
			} else {
				playButton.setEnabled(true);
				// progressBar.setVisibility(ProgressBar.GONE);
			}
		}
	}

	private class PlaySynthesizedSpeech extends Thread {
		@Override
		public void run() {
			final InputStream inputStream = new ByteArrayInputStream(
					synthesizedSpeechByteStream.toByteArray());

			int minBufferSize = 10 * AudioTrack.getMinBufferSize(16000,
					AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT);
			final int bufferSize = 20;
			AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, 16000,
					AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT, minBufferSize,
					AudioTrack.MODE_STREAM);
			byte[] s = new byte[bufferSize];
			int len = 0;
			audioTrack.play();
			try {
				while ((len = inputStream.read(s, 0, bufferSize)) > -1) {
					if (stopSynthesizedSpeech) {
						audioTrack.flush();
					} else {
						audioTrack.write(s, 0, len);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (NullPointerException e) {
				audioTrack.stop();
			} finally {
				audioTrack.stop();
				audioTrack.release();
			}
		}
	}

	static String convertStreamToString(InputStream is) {
		/*
		 * To convert the InputStream to String we use the BufferedReader.readLine() method. We iterate until
		 * the BufferedReader return null which means there's no more data to read. Each line will appended to
		 * a StringBuilder and returned as String.
		 */
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	static ByteArrayOutputStream createByteArrayOutputStream(InputStream inputStream) throws IOException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len;
		while ((len = inputStream.read(buffer)) > -1) {
			byteArrayOutputStream.write(buffer, 0, len);
		}
		return byteArrayOutputStream;
	}

}
