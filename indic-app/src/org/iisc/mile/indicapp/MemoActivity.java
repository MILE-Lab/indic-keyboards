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
import android.widget.BaseAdapter;
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
	private Cursor cursor;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.memo);
		kannadaFont = Typeface.createFromAsset(getAssets(), "fonts/Lohit-Kannada.ttf");

		memoDbAdapter = new MemoDbAdapter(this);
		memoDbAdapter.open();
		fillData();

		// // Use our own list adapter
		// setListAdapter(new MemoListAdapter(this));

		newMemoButton = (Button) findViewById(R.id.buttonNewMemo);
		newMemoButton.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				Intent intent = new Intent(getBaseContext(), TextEditorActivity.class);
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

	private void fillData() {
		cursor = memoDbAdapter.fetchAllMemos();
		startManagingCursor(cursor);
		String[] from = new String[] { MemoDbAdapter.KEY_CREATED_DATE, MemoDbAdapter.KEY_CREATED_TIME,
				MemoDbAdapter.KEY_NOTE };
		int[] to = new int[] { R.id.createdDate, R.id.createdTime, R.id.content };
		// Now create an array adapter and set it to display using our row
		SimpleCursorAdapter notes = new MemoListAdapter2(this, R.layout.memo_entry, cursor, from, to);
		setListAdapter(notes);
	}

	private class MemoListAdapter2 extends SimpleCursorAdapter {
		public MemoListAdapter2(Context context, int layout, Cursor c, String[] from, int[] to) {
			super(context, layout, c, from, to);
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			String createdDate = cursor.getString(cursor.getColumnIndex(MemoDbAdapter.KEY_CREATED_DATE));
			String createdTime = cursor.getString(cursor.getColumnIndex(MemoDbAdapter.KEY_CREATED_TIME));
			String memoNote = cursor.getString(cursor.getColumnIndex(MemoDbAdapter.KEY_NOTE));
			String memoId = cursor.getString(cursor.getColumnIndex(MemoDbAdapter.KEY_ROWID));
			int position = new Integer(memoId);

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
			String memoId = cursor.getString(cursor.getColumnIndex(MemoDbAdapter.KEY_ROWID));
			int position = new Integer(memoId);

			MemoView mv = (MemoView) view;
			mv.setCreatedDate(createdDate);
			mv.setCreatedTime(createdTime);
			mv.setContent(memoNote);
			mv.setExpanded(false);
			mv.setOnClickListener(new OnItemClickListener(position));
			mv.editButton.setOnClickListener(new OnEditMemoClickListener(position));
			mv.deleteButton.setOnClickListener(new OnDeleteMemoClickListener(position));
		}

		public void toggle(int position) {
			//mExpanded[position] = !mExpanded[position];
			notifyDataSetChanged();
		}
	}

	private class MemoListAdapter extends BaseAdapter {
		private Context mContext;
		private LayoutInflater mInflater;

		public MemoListAdapter(Context context) {
			mContext = context;
			mInflater = LayoutInflater.from(context);
		}

		public int getCount() {
			return mContents.length;
		}

		/**
		 * Since the data comes from an array, just returning the index is sufficient to get at the data. If
		 * we were using a more complex data structure, we would return whatever object represents one row in
		 * the list.
		 * 
		 * @see android.widget.ListAdapter#getItem(int)
		 */
		public Object getItem(int position) {
			return position;
		}

		/**
		 * Use the array index as a unique id.
		 * 
		 * @see android.widget.ListAdapter#getItemId(int)
		 */
		public long getItemId(int position) {
			return position;
		}

		/**
		 * Make a MemoView to hold each row.
		 * 
		 * @see android.widget.ListAdapter#getView(int, android.view.View, android.view.ViewGroup)
		 */
		public View getView(int position, View convertView, ViewGroup parent) {
			MemoView mv;
			if (convertView == null) {
				mv = new MemoView(mContext, mInflater, mDates[position], mTimes[position],
						mContents[position], mExpanded[position]);
			} else {
				mv = (MemoView) convertView;
				mv.setCreatedDate(mDates[position]);
				mv.setCreatedTime(mTimes[position]);
				mv.setContent(mContents[position]);
				mv.setExpanded(mExpanded[position]);
			}
			mv.setOnClickListener(new OnItemClickListener(position));
			mv.editButton.setOnClickListener(new OnEditMemoClickListener(position));
			mv.deleteButton.setOnClickListener(new OnDeleteMemoClickListener(position));
			return mv;
		}

		public void toggle(int position) {
			mExpanded[position] = !mExpanded[position];
			notifyDataSetChanged();
		}

		/**
		 * Our data, part 1.
		 */
		private String[] mDates = { "22/05/1961", "22/05/1967", "22/05/1973", "22/05/1977", "22/05/1983",
				"22/05/1990", "22/05/1994", "22/05/1998", "22/05/2010" };

		/**
		 * Our data, part 2.
		 */
		private String[] mTimes = { "1:31 PM", "9:53 AM", "11:02 AM", "10:20PM", "2:46 PM", "10:34 PM",
				"10:26 AM", "2:31 PM", "8:51 PM" };

		/**
		 * Our data, part 3.
		 */
		private String[] mContents = {
				"ಜ್ಞಾನಪೀಠ ಪ್ರಶಸ್ತಿ ಭಾರತದ ಸಾಹಿತಿಗಳಿಗೆ ಸಲ್ಲುವ ಅತ್ಯಂತ ಪ್ರತಿಷ್ಟಿತ ಪ್ರಶಸ್ತಿ. "
						+ "ಈ ಪ್ರಶಸ್ತಿಯು ಭಾರತದ ಸಂವಿಧಾನದ ಎಂಟನೆ ಅನುಛ್ಛೇದದಲ್ಲಿ ಉಲ್ಲೇಖವಾಗಿರುವ "
						+ "ಭಾಷೆಗಳಲ್ಲಿ ಅತ್ಯುತ್ತಮ ಸಾಹಿತ್ಯ ಕೃತಿಯನ್ನು ರಚಿಸಿದ ಭಾರತೀಯ ನಾಗರಿಕನಿಗೆ ಲಭಿಸುವುದು. "
						+ "ಈ ಪ್ರಶಸ್ತಿಯನ್ನು ಮೇ ೨೨ ೧೯೬೧ ರಲ್ಲಿ ಸ್ಥಾಪಿಸಲಾಯಿತು. ಈ ಪ್ರಶಸ್ತಿಯನ್ನು ಪ್ರಪ್ರಥಮವಾಗಿ "
						+ "೧೯೬೫ ರಲ್ಲಿ ಮಲೆಯಾಳಂ ಲೇಖಕ ಜಿ. ಶಂಕರ ಕುರುಪರಿಗೆ ಪ್ರದಾನ ಮಾಡಲಾಯಿತು. "
						+ "ವಿಜೇತರಿಗೆ ಪ್ರಶಸ್ತಿ ಫಲಕ, ಐದು ಲಕ್ಷ ರುಪಾಯಿ ನಗದು ಹಾಗು ವಾಗ್ದೇವಿಯ ಕಂಚಿನ "
						+ "ವಿಗ್ರಹವನ್ನು ನೀಡಿ ಗೌರವಿಸಲಾಗುವುದು.",

				"ಕುವೆಂಪು - ಕುಪ್ಪಳ್ಳಿ ವೆಂಕಟಪ್ಪ ಪುಟ್ಟಪ್ಪ (೧೯೦೪ - ೧೯೯೪) - "
						+ "ಕನ್ನಡವು ಪಡೆದ ಅತ್ಯುತ್ತಮ ಕವಿ, 'ರಾಷ್ಟ್ರಕವಿ'. "
						+ "ಜ್ಞಾನಪೀಠ ಪ್ರಶಸ್ತಿಯನ್ನು ಪಡೆದ ಕನ್ನಡದ ಪ್ರಥಮ ವ್ಯಕ್ತಿ. "
						+ "'ವಿಶ್ವ ಮಾನವ'. ಕನ್ನಡ ಸಾಹಿತ್ಯಕ್ಕೆ ಇವರ ಕಾಣಿಕೆ ಅಪಾರ.",

				"'ದ.ರಾ.ಬೇಂದ್ರೆ - ಕುಣಿಯೋಣು ಬಾರಾ ಕುಣಿಯೋಣು ಬಾ', " + "'ಇಳಿದು ಬಾ ತಾಯಿ ಇಳಿದು ಬಾ', "
						+ "'ನಾನು ಬಡವಿ ಆತ ಬಡವ ಒಲವೆ ನಮ್ಮ ಬದುಕು', "
						+ "ಎಂದು ಆರಂಭವಾಗುವ ಕವಿತೆಗಳನ್ನು ಕೇಳದ ಕನ್ನಡಿಗನಿಲ್ಲ. " + "ಉತ್ಸಾಹದ ಚಿಲುಮೆಯನ್ನುಕ್ಕಿಸಬಲ್ಲ, "
						+ "ನೊಂದ ಜೀವಕ್ಕೆ ಸಾಂತ್ವನ ನೀಡಬಲ್ಲ, "
						+ "ಪ್ರೀತಿ ಪ್ರೇಮಗಳನ್ನು ಮೂಡಿಸಬಲ್ಲ ಕವಿತೆಗಳನ್ನು ರಚಿಸಿಕೊಟ್ಟ ವರಕವಿ ಬೇಂದ್ರೆ. "
						+ "ರಸವೆ ಜನನ, ವಿರಸವೆ ಮರಣ, ಸಮರಸವೆ ಜೀವನ ಎಂದು ಜೀವನವನ್ನು "
						+ "ಕುರಿತು ಪರಿಣಾಮಕಾರಿಯಾಗಿ ಹೇಳಿದ ಧೀಮಂತ ಕವಿ.",

				"ಶಿವರಾಮ ಕಾರಂತ - ಜ್ಞಾನಪೀಠ ಪುರಸ್ಕೃತ ಡಾ. ಶಿವರಾಮ ಕಾರಂತರು ಹುಟ್ಟಿದ್ದು "
						+ "ಉಡುಪಿ ಜಿಲ್ಲೆಯ ಕೋಟದಲ್ಲಿ ೧೯೦೨, ಅಕ್ಟೋಬರ್ ೧೦ರಂದು. "
						+ "ಒಂದು ಶತಮಾನಕ್ಕೆ ನಾಲ್ಕು ವರ್ಷಗಳಷ್ಟೇ ಕಮ್ಮಿ ಬಾಳಿ, "
						+ "ಅರ್ಥಪೂರ್ಣ ಬದುಕು ಕಳೆದ ಸಾಹಿತ್ಯ ದಿಗ್ಗಜ ಡಾ. "
						+ "ಶಿವರಾಮ ಕಾರಂತರು ೧೯೯೭, ಡಿಸೆಂಬರ್ ೦೯ ರಂದು ನಿಧನ ಹೊಂದಿದರು. "
						+ "ತಮ್ಮ ಜೀವಿತಾವಧಿಯಲ್ಲಿ ಸುಮಾರು ೪೨೭ ಪುಸ್ತಕಗಳನ್ನು ರಚಿಸಿದರು. "
						+ "ಅವುಗಳಲ್ಲಿ ಕಾದಂಬರಿಗಳು ೪೭. ತಮ್ಮ ೯೬ನೆಯ ವಯಸ್ಸಿನಲ್ಲೂ ಹಕ್ಕಿಗಳ "
						+ "ಕುರಿತು ಒಂದು ಪುಸ್ತಕವನ್ನು ಬರೆದಿದ್ದು, ಇದು ವಿಶ್ವ ದಾಖಲೆಗೆ "
						+ "ಅರ್ಹವಾಗಿರುವ ಒಂದು ಸಾಧನೆ ಎನ್ನಬಹುದು.",

				"ಮಾಸ್ತಿ ವೆಂಕಟೇಶ ಅಯ್ಯಂಗಾರರು (ಜೂನ್ ೬ ೧೮೯೧ - ಜೂನ್ ೬ ೧೯೮೬) "
						+ "- ಕನ್ನಡದ ಒಬ್ಬ ಅಪ್ರತಿಮ ಲೇಖಕರು. ಕನ್ನಡ ಸಾಹಿತ್ಯ ಲೋಕದಲ್ಲಿ ಮಾಸ್ತಿ "
						+ "ಎಂದೇ ಖ್ಯಾತರಾಗಿರುವ ಈ ಸಾಹಿತಿ ಶ್ರೀನಿವಾಸ ಎಂಬ ಕಾವ್ಯನಾಮದಡಿಯಲ್ಲಿ ಬರೆಯುತ್ತಿದರು. "
						+ "೧೯೮೩ ರಲ್ಲಿ ಚಿಕವೀರ ರಾಜೇಂದ್ರ ಕಾದಂಬರಿಗಾಗಿ ಜ್ಞಾನಪೀಠ ಪ್ರಶಸ್ತಿಯಿಂದ "
						+ "ಪುರಸ್ಕೃತಗೊಂಡ ಮಾಸ್ತಿಯವರು ಕನ್ನಡಕ್ಕೆ ನಾಲ್ಕನೆ ಜ್ಞಾನಪೀಠ ಪ್ರಶಸ್ತಿಯನ್ನು ತಂದು ಕೊಟ್ಟರು. "
						+ "ಜೀವನ ಪರ್ಯಂತ ಕನ್ನಡ ಸೇವೆಯನ್ನು ಮಾಡಿದ ಮಾಸ್ತಿಯವರು ಜೂನ್ ೬ ೧೯೮೬ ರಂದು ನಿಧನ ಹೊಂದಿದರು.",

				"ವಿನಾಯಕ ಕೃಷ್ಣ ಗೋಕಾಕ - ಕನ್ನಡಕ್ಕೆ ಐದನೆಯ ಜ್ಞಾನಪೀಠ ಪ್ರಶಸ್ತಿಯನ್ನು ೧೯೯೧ರಲ್ಲಿ ತಂದುಕೊಟ್ಟ "
						+ "ವಿನಾಯಕ ಕೃಷ್ಣ ಗೋಕಾಕರು ಹಲವು ರೀತಿಯಲ್ಲಿ ಅದೃಷ್ಠವಂತರು. "
						+ "ಅವರು ಕನ್ನಡದ ಪ್ರತಿಭಾವಂತ ಕವಿ, ಪಂಡಿತರಾಗಿದ್ದರು. "
						+ "ಕನ್ನಡ-ಇಂಗ್ಲೀಷ್ ಭಾಷೆಗಳಲ್ಲಿ ಸಮಾನ ಪ್ರಭುತ್ವ ಪಡೆದಿದ್ದ ಅವರು "
						+ "ತಮ್ಮ ಜೀವಿತ ಕಾಲದಲ್ಲೇ ಒಬ್ಬ ಪ್ರತಿಭಾವಂತ ಸಾಹಿತಿಗೆ ದೊರಕಬೇಕಾದ "
						+ "ಎಲ್ಲ ಸಿದ್ಧಿ, ಪ್ರಸಿದ್ಧಿಗಳನ್ನು ಪಡೆದರು. "
						+ "ಗೋಕಾಕರು ಇದಕ್ಕೂ ಮೊದಲು ಭಾರತೀಯ ಜ್ಞಾನಪೀಠ ಪ್ರಶಸ್ತಿ ಆಯ್ಕೆ ಸಮಿತಿಯ ಅಧ್ಯಕ್ಷರಾಗಿದ್ದರು.",

				"ಯು.ಆರ್.ಅನಂತಮೂರ್ತಿ - ಕನ್ನಡಕ್ಕೆ ಆರನೆಯ ಜ್ಞಾನಪೀಠ ಪ್ರಶಸ್ತಿ ೧೯೯೪ರಲ್ಲಿ ಬಂದಾಗ ಕನ್ನಡ ಸಾಹಿತ್ಯ "
						+ "ಲೋಕ ಮತ್ತೊಮ್ಮೆ ಇಡೀ ದೇಶದ ಗಮನ ಸೆಳೆಯಿತು. "
						+ "ಈ ಗೌರವ ಪಡೆದವರು ಡಾ| ಉಡುಪಿ ರಾಜಗೋಪಾಲಾಚಾರ್ಯ ಅನಂತಮೂರ್ತಿ. "
						+ "ತಮ್ಮ ಬಹುಚರ್ಚಿತ ಸಂಸ್ಕಾರ ಕಾದಂಬರಿಯಿಂದ ಭಾರತೀಯ ಸಾಹಿತ್ಯ "
						+ "ಮತ್ತು ಚಲನಚಿತ್ರ ರಂಗಗಳಲ್ಲಿ ಒಂದು ದೊಡ್ಡ ವಿವಾದವನ್ನೇ ಮಾಡಿದ ಅನಂತಮೂರ್ತಿ "
						+ "ಅವರು ಹುಟ್ಟಿದ್ದು ಶಿವಮೊಗ್ಗ ಜಿಲ್ಲೆಯ ತೀರ್ಥಹಳ್ಳಿ ತಾಲ್ಲೂಕಿನ ಮೇಳಿಗೆ ಹಳ್ಳಿಯಲ್ಲಿ. "
						+ "ಇಬ್ಬರು ಜ್ಞಾನಪೀಠ ಪ್ರಶಸ್ತಿ ವಿಜೇತ (ಕುವೆಂಪು ಮತ್ತು ಅನಂತಮೂರ್ತಿ) ರನ್ನು "
						+ "ನೀಡಿದ ಹೆಗ್ಗಳಿಕೆ ತೀರ್ಥಹಳ್ಳಿ ತಾಲ್ಲೂಕಿನದು. "
						+ "ಇವರು ಹುಟ್ಟಿದ್ದು ೧೯೩೨ರ ಡಿಸೆಂಬರ್ ೨೧ರಂದು. "
						+ "ತಂದೆ ಉಡುಪಿ ರಾಜಗೋಪಾಲಾಚಾರ್ಯ. ತಾಯಿ ಸತ್ಯಮ್ಮ (ಸತ್ಯಭಾಮ).",

				"ಗಿರೀಶ್ ರಘುನಾಥ್ ಕಾರ್ನಾಡ್ (ಹುಟ್ಟು - ಮೇ ೧೯, ೧೯೩೮) "
						+ "ಕನ್ನಡಕ್ಕೆ ಏಳನೇ ಜ್ಞಾನಪೀಠ ಪ್ರಶಸ್ತಿ ತಂದುಕೊಟ್ಟ ಕನ್ನಡ "
						+ "ಸಾಹಿತ್ಯದ ನಾಟಕ ಕ್ಷೇತ್ರಕ್ಕೆ ಸಾಹಿತಿ. "
						+ "ಭಾರತದಲ್ಲೇ ನಾಟಕ ಸಾಹಿತ್ಯ ರಚನೆಗೆ ಜ್ಞಾನಪೀಠ ಪ್ರಶಸ್ತಿ "
						+ "ಪಡೆದವರಲ್ಲಿ ಕಾರ್ನಾಡ್ ಮೊದಲಿಗರು. " + "ಮತ್ತು ಇಬ್ಬರು ಜ್ಞಾನಪೀಠ ಪ್ರಶಸ್ತಿ ಪುರಸ್ಕೃತರ "
						+ "ಕಾದಂಬರಿಗಳನ್ನು ಸಿನಿಮಾ ಮಾಡಿದ ಏಕೈಕ ಜ್ಞಾನಪೀಠ ಪ್ರಶಸ್ತಿ "
						+ "ಪುರಸ್ಕೃತರೆಂಬ ಗೌರವಕ್ಕೆ ಕಾರ್ನಾಡ್ ಪಾತ್ರರಾಗಿದ್ದಾರೆ.",

				"ಡಾ. ಚಂದ್ರಶೇಖರ ಕಂಬಾರ (ಜನನ- ೨ ಜನವರಿ ೧೯೩೭)ರಂದು "
						+ "ಬೆಳಗಾವಿ ಜಿಲ್ಲೆ ಘೋಡಿಗೇರಿ ಗ್ರಾಮದ ಬಸವಣ್ಣೆಪ್ಪ ಕಂಬಾರ ಹಾಗೂ ಚೆನ್ನಮ್ಮ ದಂಪತಿಯ "
						+ "ಪುತ್ರನಾಗಿ ಜನಿಸಿದರು,ಬೆಳಗಾವಿ ಲಿಂಗರಾಜ ಕಾಲೇಜಿನಲ್ಲಿ ಬಿಎ, "
						+ "೧೯೬೨ರಲ್ಲಿ ಕರ್ನಾಟಕ ವಿವಿಯಿಂದ ಎಂ.ಎ ಪದವಿ,ಪಿ.ಎಚ್.ಡಿ.ಪದವಿ ಪಡೆದಿರುವ ಅವರು, "
						+ "ಬೆಂಗಳೂರು ವಿವಿ ಅಧ್ಯಪಕರು, ಕರ್ನಾಟಕ ಜಾನಪದ ಅಕಾಡೆಮಿಯ ಅಧ್ಯಕ್ಷ, "
						+ "ನವದೆಹಲಿಯ ರಾಷ್ಟ್ರೀಯ ನಾಟಕ ಶಾಲೆಯ ನಿರ್ದೇಶಕ,ಹಂಪಿ ಕನ್ನಡ ವಿವಿಯ "
						+ "ಮೊದಲ ಕುಲಪತಿಯಾಗಿ ಡಾ. ಚಂದ್ರಶೇಖರ ಕಂಬಾರ ಕಾರ್ಯನಿರ್ವಹಿಸಿದ್ದಾರೆ." };

		/**
		 * Our data, part 4.
		 */
		private boolean[] mExpanded = { false, false, false, false, false, false, false, false, false };
	}

	private class OnItemClickListener implements OnClickListener {
		private int mPosition;

		OnItemClickListener(int position) {
			mPosition = position;
		}

		public void onClick(View arg0) {
			((MemoListAdapter2) getListAdapter()).toggle(mPosition);
		}
	}

	private class OnEditMemoClickListener implements OnClickListener {
		private int mPosition;

		OnEditMemoClickListener(int position) {
			mPosition = position;
		}

		public void onClick(View arg0) {
			Toast.makeText(getBaseContext(), "Edit item " + mPosition, Toast.LENGTH_SHORT).show();
		}
	}

	private class OnDeleteMemoClickListener implements OnClickListener {
		private int mPosition;

		OnDeleteMemoClickListener(int position) {
			mPosition = position;
		}

		public void onClick(View arg0) {
			Toast.makeText(getBaseContext(), "Delete item " + mPosition, Toast.LENGTH_SHORT).show();
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
