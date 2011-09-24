package org.iisc.mile.indicapp;

import java.io.File;
import java.io.FilenameFilter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class TextEditorActivity extends Activity {

	private EditText indicTextEditor;

	private Button buttonNew;
	private Button buttonOpen;
	private Button buttonSave;
	private Button buttonCancel;

	private static final int OPEN_DIALOG = 0;
	private static final int SAVE_DIALOG = 1;

	private static final String EDITOR_CONTENT = "Current contents of text editor";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.text_editor);

		indicTextEditor = (EditText) findViewById(R.id.textEditor);
		indicTextEditor.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Lohit-Kannada.ttf"));

		if (savedInstanceState != null) {
			String previousEditorContent = savedInstanceState.getString(EDITOR_CONTENT);
			if (previousEditorContent != null) {
				indicTextEditor.setText(previousEditorContent);
			}
		}

		buttonNew = (Button) findViewById(R.id.buttonNew);
		buttonNew.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Toast.makeText(getBaseContext(), "New button clicked", Toast.LENGTH_SHORT).show();
			}
		});

		buttonOpen = (Button) findViewById(R.id.buttonOpen);
		buttonOpen.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				showDialog(OPEN_DIALOG);
			}
		});

		buttonSave = (Button) findViewById(R.id.buttonSave);
		buttonSave.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				showDialog(SAVE_DIALOG);
			}
		});

		buttonCancel = (Button) findViewById(R.id.buttonCancel);
		buttonCancel.setOnClickListener(new OnClickListener() {
			@Override
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
		MenuItem mnu1 = menu.add(0, 0, 0, "Font");
		MenuItem mnu2 = menu.add(0, 0, 0, "Delete");
		MenuItem mnu3 = menu.add(0, 0, 0, "Help");
		mnu1.setIcon(R.drawable.fonts);
		mnu2.setIcon(R.drawable.delete);
		mnu3.setIcon(R.drawable.help);
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
		case OPEN_DIALOG:
			loadFileList();
			return getOpenDialog();
		default:
			return null;
		}
	}

	private String[] mFileList;
	private File mPath = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
	private static final String FTYPE = ".txt";

	private void loadFileList() {
		if (mPath.exists()) {
			FilenameFilter filter = new FilenameFilter() {
				public boolean accept(File dir, String filename) {
					File sel = new File(dir, filename);
					return filename.contains(FTYPE) || sel.isDirectory();
				}
			};
			mFileList = mPath.list(filter);
		} else {
			mFileList = new String[0];
		}
	}

	private Dialog getOpenDialog() {
		ListAdapter adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.file_dialog_row,
				mFileList) {
			ViewHolder holder;
			Drawable directoryIcon = getResources().getDrawable(R.drawable.folder);
			Drawable textFileIcon = getResources().getDrawable(R.drawable.file_text);
			Drawable fontFileIcon = getResources().getDrawable(R.drawable.file_font);

			class ViewHolder {
				ImageView icon;
				TextView title;
			}

			public View getView(int position, View convertView, ViewGroup parent) {
				final LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(
						Context.LAYOUT_INFLATER_SERVICE);

				if (convertView == null) {
					convertView = inflater.inflate(R.layout.file_dialog_row, null);

					holder = new ViewHolder();
					holder.icon = (ImageView) convertView.findViewById(R.id.icon);
					holder.title = (TextView) convertView.findViewById(R.id.title);
					convertView.setTag(holder);
				} else {
					// view already defined, retrieve view holder
					holder = (ViewHolder) convertView.getTag();
				}

				String currentItem = mFileList[position];
				File tempFile = new File(mPath, currentItem);

				holder.title.setText(currentItem);
				if (tempFile.isDirectory()) {
					holder.icon.setImageDrawable(directoryIcon);
				} else if (tempFile.isFile()) {
					holder.icon.setImageDrawable(textFileIcon);
				}

				return convertView;
			}
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Choose your file");
		builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {
				Toast.makeText(getBaseContext(), "You selected: " + mFileList[item], Toast.LENGTH_LONG)
						.show();
				dialog.dismiss();
			}
		});
		return builder.show();
	}

}