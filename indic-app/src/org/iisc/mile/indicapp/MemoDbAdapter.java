package org.iisc.mile.indicapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MemoDbAdapter {
	// Android SQLite Database Tutorial by Lars Vogel
	// http://www.vogella.de/articles/AndroidSQLite/article.html

	private static final String TAG = "MemoDataHandler";

	private static final String DATABASE_NAME = "indicAppMemo.db";
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_TABLE = "memo";

	public static final String KEY_ROWID = "_id";
	public static final String KEY_NOTE = "note";
	public static final String KEY_CREATED_DATE = "created_date";
	public static final String KEY_CREATED_TIME = "created_time";

	/**
	 * This class helps open, create, and upgrade the database file.
	 */
	private static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE " + DATABASE_TABLE + " (" + KEY_ROWID + " INTEGER PRIMARY KEY,"
					+ KEY_NOTE + " TEXT," + KEY_CREATED_DATE + " TEXT," + KEY_CREATED_TIME + " TEXT" + ");");
			for (int i = 0; i < mDates.length; i++) {
				ContentValues initialValues = createContentValues(mContents[i], mDates[i], mTimes[i]);
				db.insert(DATABASE_TABLE, null, initialValues);
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion
					+ ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS" + DATABASE_TABLE);
			onCreate(db);
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

	private Context context;
	private SQLiteDatabase database;
	private DatabaseHelper dbHelper;

	public MemoDbAdapter(Context context) {
		this.context = context;
	}

	public MemoDbAdapter open() throws SQLException {
		dbHelper = new DatabaseHelper(context);
		database = dbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		dbHelper.close();
	}

	/**
	 * Create a new Memo. If the Memo is successfully created then return the new rowId for that note,
	 * otherwise return a -1 to indicate failure.
	 */
	public long createMemo(String note, String createdDate, String createdTime) {
		ContentValues initialValues = createContentValues(note, createdDate, createdTime);

		return database.insert(DATABASE_TABLE, null, initialValues);
	}

	public boolean updateMemo(long rowId, String note, String createdDate, String createdTime) {
		ContentValues updateValues = createContentValues(note, createdDate, createdTime);

		return database.update(DATABASE_TABLE, updateValues, KEY_ROWID + "=" + rowId, null) > 0;
	}

	public boolean deleteMemo(long rowId) {
		return database.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}

	/**
	 * Return a Cursor over the list of all Memo in the database.
	 * 
	 * @return Cursor over all notes
	 */
	public Cursor fetchAllMemos() {
		return database.query(DATABASE_TABLE, new String[] { KEY_ROWID, KEY_NOTE, KEY_CREATED_DATE,
				KEY_CREATED_TIME }, null, null, null, null, KEY_ROWID + " DESC");
	}

	/** * Return a Cursor positioned at the defined Memo */
	public Cursor fetchMemo(long rowId) throws SQLException {
		Cursor mCursor = database.query(true, DATABASE_TABLE, new String[] { KEY_ROWID, KEY_NOTE,
				KEY_CREATED_DATE, KEY_CREATED_TIME }, KEY_ROWID + "=" + rowId, null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	static ContentValues createContentValues(String note, String createdDate, String createdTime) {
		ContentValues values = new ContentValues();
		values.put(KEY_NOTE, note);
		values.put(KEY_CREATED_DATE, createdDate);
		values.put(KEY_CREATED_TIME, createdTime);
		return values;
	}
}
