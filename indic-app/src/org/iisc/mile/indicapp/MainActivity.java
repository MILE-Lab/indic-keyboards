package org.iisc.mile.indicapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class MainActivity extends Activity {

	private ImageButton textEditorButton;
	private ImageButton browserButton;
	private ImageButton messagingButton;
	private ImageButton aboutButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		textEditorButton = (ImageButton) findViewById(R.id.imageButtonTextEditor);
		browserButton = (ImageButton) findViewById(R.id.imageButtonBrowser);
		messagingButton = (ImageButton) findViewById(R.id.imageButtonMessaging);
		aboutButton = (ImageButton) findViewById(R.id.imageButtonAbout);

		textEditorButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(getBaseContext(), TextEditorActivity.class);
				startActivity(intent);
			}
		});

		messagingButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
			}
		});

		browserButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
			}
		});

		aboutButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
			}
		});
	}
}