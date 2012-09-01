package org.iisc.mile.indicapp;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TextEditorActivity extends Activity {
	private EditText indicTextEditor;
	private Button buttonSave;
	private Button buttonCancel;

	int memoId;
	String memoNote;
	String memoCreatedDate;
	String memoCreatedTime;

	private static final String EDITOR_CONTENT = "Current contents of text editor";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.text_editor);

		indicTextEditor = (EditText) findViewById(R.id.textEditor);

		if (savedInstanceState != null) {
			String previousEditorContent = savedInstanceState.getString(EDITOR_CONTENT);
			if (previousEditorContent != null) {
				indicTextEditor.setText(previousEditorContent);
			}
		} else {
			Bundle bundle = getIntent().getExtras();
			if (bundle != null) {
				memoId = bundle.getInt(MemoDbAdapter.KEY_ROWID);
				memoNote = bundle.getString(MemoDbAdapter.KEY_NOTE);
				memoCreatedDate = bundle.getString(MemoDbAdapter.KEY_CREATED_DATE);
				memoCreatedTime = bundle.getString(MemoDbAdapter.KEY_CREATED_TIME);
				if (memoNote != null) {
					indicTextEditor.setText(memoNote);
				}
			} else {
				memoId = -1;
			}
		}

		buttonSave = (Button) findViewById(R.id.buttonSave);
		buttonSave.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
			}
		});

		buttonCancel = (Button) findViewById(R.id.buttonCancel);
		buttonCancel.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				finish();
			}
		});
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// Save the editor text, so we still have it if the activity needs to be killed while paused.
		outState.putString(EDITOR_CONTENT, indicTextEditor.getText().toString());
		super.onSaveInstanceState(outState);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuItem mnu1 = menu.add(0, 0, 0, "Color");
		MenuItem mnu2 = menu.add(0, 0, 0, "Delete");
		mnu1.setIcon(R.drawable.fonts);
		mnu2.setIcon(R.drawable.delete);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Toast.makeText(getBaseContext(), item.getTitle() + " menu selected", Toast.LENGTH_SHORT).show();
		return true;
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		default:
			return null;
		}
	}

}