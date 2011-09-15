package org.iisc.mile.indictext.android;

import org.iisc.mile.indictext.android.R;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.EditText;

public class MainActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		Typeface fontHindi = Typeface.createFromAsset(getAssets(), "Lohit-Devanagari.ttf");
		final EditText editBoxHindi = (EditText) findViewById(R.id.editBoxHindi);
		editBoxHindi.setTypeface(fontHindi);
		Typeface fontKannada = Typeface.createFromAsset(getAssets(), "Lohit-Kannada.ttf");
		final EditText editBoxKannada = (EditText) findViewById(R.id.editBoxKannada);
		editBoxKannada.setTypeface(fontKannada);
		Typeface fontTamil = Typeface.createFromAsset(getAssets(), "Lohit-Tamil.ttf");
		final EditText editBoxTamil = (EditText) findViewById(R.id.editBoxTamil);
		editBoxTamil.setTypeface(fontTamil);
	}
}