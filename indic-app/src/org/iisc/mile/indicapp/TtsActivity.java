package org.iisc.mile.indicapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.speech.tts.TextToSpeech.OnInitListener;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Scroller;
import android.widget.Toast;

public class TtsActivity extends Activity implements OnClickListener {

	Button speakButton;
	EditText enteredText;
	String result, status, text;
	int i;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_first);
		speakButton = (Button) findViewById(R.id.speak);
		enteredText = (EditText) findViewById(R.id.enter);
		enteredText.setScroller(new Scroller(getApplicationContext()));
		enteredText.setVerticalScrollBarEnabled(true);
		enteredText.setMovementMethod(new ScrollingMovementMethod());
		speakButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Log.i("accept string", " done");
				
				
				text = enteredText.getText().toString();
				speakWords(text);
			}
		});
	}

	

	private void speakWords(String text) {
		new DoPostRequestAsync().execute();
		Log.i("post", "send");
	}

	private class DoPostRequestAsync extends AsyncTask<URL, Void, String> {

		@Override
		protected String doInBackground(URL... arg0) {
			// Create a new HttpClient and Post Header
			HttpPost httppost = new HttpPost(
					"http://mile.ee.iisc.ernet.in:8080/tts_demo/rest");
			HttpParams httpParameters = new BasicHttpParams();
			// Set the timeout in milliseconds until a connection is
			// established.
			// The default value is zero, that means the timeout is not used.
			int timeoutConnection = 5000;
			HttpConnectionParams.setConnectionTimeout(httpParameters,
					timeoutConnection);
			// Set the default socket timeout (SO_TIMEOUT)
			// in milliseconds which is the timeout for waiting for data.
			int timeoutSocket = 5000;
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

			DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);
			try {

				StringEntity stringEntity = new StringEntity(text, "UTF-8");
				httppost.setEntity(stringEntity);

				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
				if (entity != null) {

					// A Simple JSON Response Read
					InputStream instream = entity.getContent();
					result = convertStreamToString(instream);
				}

				status = response.getStatusLine().toString();

			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
			} catch (IOException e) {
				// TODO Auto-generated catch block
			}

			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			// This gets called on the main thread!
			if (result == null){
				Toast.makeText(getApplicationContext(),
						"TIME OUT!!!! \n please check internet connection ",
						Toast.LENGTH_LONG).show();
				
				return;
				}
			else {
				//result=result.substring(result.indexOf(""), result.indexOf(""));
				String address = result.substring(77);
				i = 0;
				
				while (address.charAt(i) != '>') {
					i++;
				}
				result = address.substring(0, i - 1);
				// execute mp3 player
				Log.i("recieved",result);
				callintent(result, status);
			}
		}
	}

	public void callintent(String address, String status2) {
		// TODO Auto-generated method stub

		Intent intent = new Intent("android.intent.action.SoundPlayer");
		intent.putExtra("result", address);
		intent.putExtra("status", status2);
		intent.putExtra("value",text);
		startActivity(intent);

	}

	private static String convertStreamToString(InputStream is) {
		/*
		 * To convert the InputStream to String we use the
		 * BufferedReader.readLine() method. We iterate until the BufferedReader
		 * return null which means there's no more data to read. Each line will
		 * appended to a StringBuilder and returned as String.
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

	public void onInit(int arg0) {
		// TODO Auto-generated method stub

	}



	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
}
