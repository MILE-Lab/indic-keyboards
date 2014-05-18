package org.iisc.mile.indickeyboards.android;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class AboutActivity extends Activity {
	private static final String TAG = AboutActivity.class.getCanonicalName();
	TextView textView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		textView = (TextView) findViewById(R.id.about_text);
		textView.setText(getAboutText());
	}

	public String getAboutText() {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("IISc MILE Indic Keyboards");
		stringBuffer.append(getVersionInfo());
		stringBuffer.append("\n\n(c) Copyright 2011-2014 MILE lab, IISc");
		stringBuffer.append("\nVisit http://mile.ee.iisc.ernet.in");

		stringBuffer.append("\n\nCredits:");
		stringBuffer.append("\nIdea/Conceptualization by:");
		stringBuffer.append("\n  Shiva Kumar H R <shivahr@gmail.com>");
		stringBuffer.append("\n  Prof. A G Ramakrishnan <agrkrish@gmail.com>");
		stringBuffer.append("\n\nDevelopers:");
		stringBuffer.append("\n  Shiva Kumar H R <shivahr@gmail.com>");
		stringBuffer.append("\n  Satvik Neelakant <nsatvik@gmail.com>");
		stringBuffer.append("\n  Shruthi R <shruthir.colours@gmail.com>");
		stringBuffer.append("\n  Priya S <priyasnb@gmail.com>");
		stringBuffer.append("\n  Sandesh S <sandy.s2991@gmail.com>");
		return stringBuffer.toString();
	}

	public String getVersionInfo() {
		try {
			final PackageInfo info = getPackageInfo(getApplicationContext());
			return "\nVersion: " + info.versionName + " (release " + info.versionCode + ")";
		} catch (final NameNotFoundException e) {
			Log.e(TAG, "Failed to locate package information!");
		}
		return "";
	}

	public PackageInfo getPackageInfo(Context context) throws NameNotFoundException {
		return context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
	}

}
