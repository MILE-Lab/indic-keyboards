/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.iisc.mile.indicapp;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MemoActivity extends ListActivity {
	private Button newMemoButton;
	Typeface kannadaFont;

	private MemoDbAdapter memoDbAdapter;
	SimpleCursorAdapter listAdapter;
	private Cursor cursor;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.memo);
		kannadaFont = Typeface.createFromAsset(getAssets(), "fonts/Lohit-Kannada.ttf");

		memoDbAdapter = new MemoDbAdapter(this);
		memoDbAdapter.open();
		cursor = memoDbAdapter.fetchAllMemos();
		startManagingCursor(cursor);
		String[] from = new String[] { MemoDbAdapter.KEY_CREATED_DATE, MemoDbAdapter.KEY_CREATED_TIME,
				MemoDbAdapter.KEY_NOTE };
		int[] to = new int[] { R.id.createdDate, R.id.createdTime, R.id.content };
		// Now create an array adapter and set it to display using our row
		listAdapter = new MemoListAdapter(this, R.layout.memo_entry, cursor, from, to);
		setListAdapter(listAdapter);

		newMemoButton = (Button) findViewById(R.id.buttonNewMemo);
		newMemoButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				Intent intent = new Intent(MemoActivity.this, TextEditorActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (memoDbAdapter != null) {
			memoDbAdapter.close();
		}
	}

	private class MemoListAdapter extends SimpleCursorAdapter {
		public MemoListAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
			super(context, layout, c, from, to);
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			String createdDate = cursor.getString(cursor.getColumnIndex(MemoDbAdapter.KEY_CREATED_DATE));
			String createdTime = cursor.getString(cursor.getColumnIndex(MemoDbAdapter.KEY_CREATED_TIME));
			String memoNote = cursor.getString(cursor.getColumnIndex(MemoDbAdapter.KEY_NOTE));
			String memoId = cursor.getString(cursor.getColumnIndex(MemoDbAdapter.KEY_ROWID));
			int position = Integer.valueOf(memoId);

			MemoView mv = new MemoView(context, LayoutInflater.from(context), createdDate, createdTime,
					memoNote, false);
			mv.setOnClickListener(new OnItemClickListener(position));
			mv.editButton.setOnClickListener(new OnEditMemoClickListener(position));
			mv.deleteButton.setOnClickListener(new OnDeleteMemoClickListener(position));
			return mv;
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			String createdDate = cursor.getString(cursor.getColumnIndex(MemoDbAdapter.KEY_CREATED_DATE));
			String createdTime = cursor.getString(cursor.getColumnIndex(MemoDbAdapter.KEY_CREATED_TIME));
			String memoNote = cursor.getString(cursor.getColumnIndex(MemoDbAdapter.KEY_NOTE));
			int memoId = Integer.valueOf(cursor.getString(cursor.getColumnIndex(MemoDbAdapter.KEY_ROWID)));

			MemoView mv = (MemoView) view;
			mv.setCreatedDate(createdDate);
			mv.setCreatedTime(createdTime);
			mv.setContent(memoNote);
			mv.setExpanded(false);
			mv.setOnClickListener(new OnItemClickListener(memoId));
			mv.editButton.setOnClickListener(new OnEditMemoClickListener(memoId));
			mv.deleteButton.setOnClickListener(new OnDeleteMemoClickListener(memoId));
		}

		public void toggle(int position) {
			// mExpanded[position] = !mExpanded[position];
			notifyDataSetChanged();
		}
	}

	private class OnItemClickListener implements OnClickListener {
		private int memoId;

		OnItemClickListener(int _memoId) {
			memoId = _memoId;
		}

		public void onClick(View arg0) {
			((MemoListAdapter) getListAdapter()).toggle(memoId);
		}
	}

	private class OnEditMemoClickListener implements OnClickListener {
		private int memoId;

		OnEditMemoClickListener(int _memoId) {
			memoId = _memoId;
		}

		public void onClick(View arg0) {
			Toast.makeText(getBaseContext(), "Edit item " + memoId, Toast.LENGTH_SHORT).show();

			Cursor cursor = memoDbAdapter.fetchMemo(memoId);
			String note = cursor.getString(1);
			String date = cursor.getString(2);
			String time = cursor.getString(3);
			cursor.close();

			Intent intent = new Intent(MemoActivity.this, TextEditorActivity.class);
			Bundle bundle = new Bundle();
			bundle.putInt(MemoDbAdapter.KEY_ROWID, memoId);
			bundle.putString(MemoDbAdapter.KEY_NOTE, note);
			bundle.putString(MemoDbAdapter.KEY_CREATED_DATE, date);
			bundle.putString(MemoDbAdapter.KEY_CREATED_TIME, time);
			intent.putExtras(bundle);
			startActivityForResult(intent, 110);
		}
	}

	private class OnDeleteMemoClickListener implements OnClickListener {
		private int memoId;

		OnDeleteMemoClickListener(int _memoId) {
			memoId = _memoId;
		}

		public void onClick(View arg0) {
			Toast.makeText(getBaseContext(), "Delete item " + (memoId + 1), Toast.LENGTH_SHORT).show();
			memoDbAdapter.deleteMemo(memoId);
			listAdapter.changeCursor(memoDbAdapter.fetchAllMemos());
		}
	}

	private class MemoView extends LinearLayout {
		ImageButton editButton;
		ImageButton deleteButton;
		private TextView createdDate;
		private TextView createdTime;
		private TextView content;

		public MemoView(Context context, LayoutInflater inflater, String _createdDate, String _createdTime,
				String _content, boolean expanded) {
			super(context);
			View inflatedView = inflater.inflate(R.layout.memo_entry, null);

			editButton = (ImageButton) inflatedView.findViewById(R.id.editIcon);
			deleteButton = (ImageButton) inflatedView.findViewById(R.id.deleteIcon);

			createdDate = (TextView) inflatedView.findViewById(R.id.createdDate);
			createdDate.setText(_createdDate);

			createdTime = (TextView) inflatedView.findViewById(R.id.createdTime);
			createdTime.setText(_createdTime);

			content = (TextView) inflatedView.findViewById(R.id.content);
			content.setTypeface(kannadaFont);
			content.setText(_content);
			content.setSingleLine(expanded ? false : true);

			addView(inflatedView, new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.FILL_PARENT));
		}

		public void setCreatedDate(String _createdDate) {
			createdDate.setText(_createdDate);
		}

		public void setCreatedTime(String _createdTime) {
			createdTime.setText(_createdTime);
		}

		public void setContent(String _content) {
			content.setText(_content);
		}

		public void setExpanded(boolean expanded) {
			content.setSingleLine(expanded ? false : true);
		}
	}
}
