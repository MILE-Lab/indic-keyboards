package org.iisc.mile.indicapp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import android.app.Activity;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

public class SoundPlayer extends Activity implements OnPreparedListener
{
	protected static final int INTIAL_KB_BUFFER = 0;
	InputStream instream;
	Boolean flag = false;
	String address, status, first = "http://mile.ee.iisc.ernet.in:8080", path,stringText;
	ProgressBar pb;
	Button play, stop, ret;
	TextView textBox;
	static ByteArrayOutputStream bArray;
	int stopflag=1,	cont=1,spinner=1;
	


	@Override
	protected void onStart() {
		stopflag=1;
		super.onStart();
	}


	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		stopflag=0;
		super.onStop();
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {

		setContentView(R.layout.sound_player);
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		status = intent.getStringExtra("status");
		address = intent.getStringExtra("result");
		stringText=intent.getStringExtra("value");
		instream=null;
		stopflag=1;
		cont=1;
		textBox=(TextView)findViewById(R.id.textBox);
		textBox.setScroller(new Scroller(getApplicationContext()));
		textBox.setVerticalScrollBarEnabled(true);
		textBox.setMovementMethod(new ScrollingMovementMethod());
		pb=(ProgressBar)findViewById(R.id.pb);
		play = (Button)findViewById(R.id.play);
		ret = (Button) findViewById(R.id.ret);
		
		checkstatus(status, address);
		
		play.setEnabled(false);
		ret.setEnabled(true);
		textBox.setText(stringText);
		
		// setting up the buttons.....	
		play.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				play();
			}
		});
		ret.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				instream=null;

				finish();
			}
		});
	}
	

	private void checkstatus(String status2, String address) {
		String okStatus = "HTTP/1.0 200 OK";
		int flag = okStatus.compareTo(status2);
		if (flag >= 0) {
			// synth sucess
			path = first + address;
			Log.i("text", path);
			Toast.makeText(getApplicationContext(),
					"success",
					Toast.LENGTH_LONG).show();
			new DoGetRequestAsync().execute();
		}
		// synth fail
		else {
			Toast.makeText(getApplicationContext(),
					"synthesis failed!! please try again......",
					Toast.LENGTH_LONG).show();
				finish();
		}
	}

	// for the path append the string "http://mile.ee.iisc.ernet.in:8080"
	// full address is first+address
	private class DoGetRequestAsync extends AsyncTask<URL, Void, String> {

		@Override
		protected String doInBackground(URL... arg0) {

			HttpGet httpGet = new HttpGet(path);
			HttpParams httpParameters = new BasicHttpParams();
			int timeoutConnection = 3000;
			HttpConnectionParams.setConnectionTimeout(httpParameters,
					timeoutConnection);
			int timeoutSocket = 5000;
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

			DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);
			String result = "test";
			try {

				HttpResponse response = httpclient.execute(httpGet);
				HttpEntity entity = response.getEntity();
				if (entity != null) {

					instream = entity.getContent();
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			bArray = new ByteArrayOutputStream();
			try {
				copy(instream);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return result;
		}
		

		@Override
		protected void onPostExecute(String result) {
			if (result == null){
				Toast.makeText(getApplicationContext(),
						"TIME OUT!!!! please check internet connectivity",
						Toast.LENGTH_LONG).show();
				finish();
			}
			else {
				
				Log.i(result, "recieved !!");
				spinner=0;
				play.setEnabled(true);
				pb.setVisibility(ProgressBar.GONE);
			}
		}
	}


	 public void playWav() throws IOException {
		
		 final InputStream is1 = new ByteArrayInputStream(bArray.toByteArray()); 
	        final Thread thread = new Thread()
			{
			@Override
				public void run(){
			
					  int minBufferSize = AudioTrack.getMinBufferSize(16000, AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT);
					    final int bufferSize = 20;
					    minBufferSize =minBufferSize*10;
				 Log.i("val","val"+ minBufferSize);
				   
				 AudioTrack  at = new AudioTrack(AudioManager.STREAM_MUSIC, 16000, AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT, minBufferSize, AudioTrack.MODE_STREAM);
					int i = 0;
					byte[] s = new byte[bufferSize];
					while (cont==0)
					{
						
					}
					at.play();
					cont=0;
						
					
					try {
						while((i = is1.read(s, 0, bufferSize)) > -1){
							if(stopflag==0)
							{
							at.flush();
							}else{
								at.write(s, 0, i);
							}
							}
					} catch (IOException e) {
						e.printStackTrace();
					}catch (NullPointerException e) {
						at.stop();
					}
					finally{
						cont=1;
						at.stop();
						at.release();
					
					}
				}	
			};
			thread.start();
	}
	
	
	private void play() {
		try { 
				playWav();	
		}
		catch(IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
				
	}

	public static int copy(InputStream input) throws IOException{
		 byte[] buffer = new byte[1024];
		    int len;
		    while ((len = input.read(buffer)) > -1 ) {
		       bArray.write(buffer, 0, len);
		    }
		    bArray.flush();
		    return 0;
	 }
	public void onPrepared(MediaPlayer arg0) {
		//empty method
	}
}
