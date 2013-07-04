package org.iisc.mile.indicapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class MainActivity extends Activity {

	private ImageButton messagingButton;
	private ImageButton memoButton;
	private ImageButton ttsButton;
	private ImageButton aboutButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		messagingButton = (ImageButton) findViewById(R.id.imageButtonMessaging);
		memoButton = (ImageButton) findViewById(R.id.imageButtonTextEditor);
		ttsButton = (ImageButton) findViewById(R.id.imageButtonTextToSpeech);
		aboutButton = (ImageButton) findViewById(R.id.imageButtonAbout);

		messagingButton.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				Intent intent = new Intent(getBaseContext(), MessageActivity.class);
				startActivity(intent);
			}
		});

		memoButton.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				Intent intent = new Intent(getBaseContext(), MemoActivity.class);
				startActivity(intent);
			}
		});

		ttsButton.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				Intent intent = new Intent(getBaseContext(), TtsActivity.class);
				startActivity(intent);
			}
		});

		aboutButton.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
			}
		});
	}
}