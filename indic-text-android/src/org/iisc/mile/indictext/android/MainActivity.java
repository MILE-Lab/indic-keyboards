package org.iisc.mile.indictext.android;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.EditText;

public class MainActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

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