/*
 * Copyright (C) 2008-2009 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.iisc.mile.indickeyboards.android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.method.MetaKeyKeyListener;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.CompletionInfo;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.Toast;

public class SoftKeyboard extends InputMethodService implements KeyboardView.OnKeyboardActionListener {
	static final boolean DEBUG = false;

	/**
	 * This boolean indicates the optional example code for performing processing of hard keys in addition to
	 * regular text generation from on-screen interaction. It would be used for input methods that perform
	 * language translations (such as converting text entered on a QWERTY keyboard to Chinese), but may not be
	 * used for input methods that are primarily intended to be used for on-screen text entry.
	 */
	static final boolean PROCESS_HARD_KEYS = true;

	public static final int SETTINGS_KEYCODE = 0xF000;
	public static final int KAGAPA_LETTERS_TO_SYMBOLS_KEYCODE = 0xF001;
	public static final int KANNADA_INSCRIPT_LETTERS_TO_SYMBOLS_KEYCODE = 0xF002;
	public static final int KANNADA_3X4_LETTERS_TO_NUMBERS_KEYCODE = 0xF003;
	public static final int KANNADA_3X4_LETTERS_TO_SYMBOLS_KEYCODE = 0xF004;
	public static final int PHONETIC_LETTERS_TO_SYMBOLS_KEYCODE = 0xF005;
	public static final int HINDI_REMINGTON_LETTERS_TO_SYMBOLS_KEYCODE = 0xF101;
	public static final int HINDI_3X4_LETTERS_TO_NUMBERS_KEYCODE = 0xF102;
	public static final int HINDI_3X4_LETTERS_TO_SYMBOLS_KEYCODE = 0xF103;
	public static final int HINDI_INSCRIPT_LETTERS_TO_SYMBOLS_KEYCODE = 0xF104;
	public static final int TAMIL_3X4_LETTERS_TO_NUMBERS_KEYCODE = 0xF201;
	public static final int TAMIL_3X4_LETTERS_TO_SYMBOLS_KEYCODE = 0xF202;

	public static final int KSHA_COMPOUND_LETTER = 0xF010;
	public static final int ARKAAOTTU_COMPOUND_LETTER = 0xF011;
	public static final int JNYA_COMPOUND_LETTER = 0xF012;
	public static final int OTTU_R_COMPOUND_LETTER = 0xF013;
	public static final int TRA_COMPOUND_LETTER = 0xF014;
	public static final int SHRA_COMPOUND_LETTER = 0xF015;

	String[] KANNADA_LAYOUT_CHOICES = new String[] { "KaGaPa", "InScript", "3x4 Keyboard", "Phonetic" };
	private static final int KB_KAGAPA = 0;
	private static final int KB_KANNADA_INSCRIPT = 1;
	private static final int KB_KANNADA_3x4 = 2;
	private static final int KB_KANNADA_PHONETIC = 3;

	String[] HINDI_LAYOUT_CHOICES = new String[] { "Remington", "InScript", "3x4 Keyboard", "Phonetic" };
	private static final int KB_HINDI_REMINGTON = 0;
	private static final int KB_HINDI_INSCRIPT = 1;
	private static final int KB_HINDI_3x4 = 2;
	private static final int KB_HINDI_PHONETIC = 3;

	String[] TAMIL_LAYOUT_CHOICES = new String[] { "3x4 Keyboard", "Phonetic" };
	// private static final int KB_TAMILNET99 = 0;
	// private static final int KB_TAMIL_INSCRIPT = 1;
	// private static final int KB_TAMIL_REMINGTON = 2;
	private static final int KB_TAMIL_3x4 = 0;
	private static final int KB_TAMIL_PHONETIC = 1;

	String[] LANGUAGE_CHOICES = new String[] { "Hindi", "Kannada", "Tamil" };
	private static final int KB_LANGUAGE_HINDI = 0;
	private static final int KB_LANGUAGE_KANNADA = 1;
	private static final int KB_LANGUAGE_TAMIL = 2;

	private SharedPreferences mSharedPreferences;
	private String KEYBOARD_PREFERENCES = "IISc MILE Indic Keyboards Preferences";
	private static final String KB_CURRENT_LANGUAGE = "Current Keyboard Language";
	private static final String KB_CURRENT_KANNADA_LAYOUT = "Current Kannada Keyboard Layout";
	private static final String KB_CURRENT_HINDI_LAYOUT = "Current Hindi Keyboard Layout";
	private static final String KB_CURRENT_TAMIL_LAYOUT = "Current Tamil Keyboard Layout";

	private static final int KB_DEFAULT_LANGUAGE = KB_LANGUAGE_KANNADA;
	private static final int KB_DEFAULT_LAYOUT = KB_KAGAPA;

	private KeyboardView mInputView;
	private CandidateView mCandidateView;
	private CompletionInfo[] mCompletions;

	private static final int SHIFT_STATE_INITIAL = 1;
	private static final int SHIFT_STATE_INTERMEDIATE = 2;
	private static final int SHIFT_STATE_FINAL = 3;
	private int shiftState;

	private StringBuilder mComposing = new StringBuilder();
	private boolean mPredictionOn;
	private boolean mCompletionOn;
	private int mLastDisplayWidth;
	private boolean mCapsLock;
	private long mLastShiftTime;
	private long mMetaState;

	private LatinKeyboard mPhoneticSymbolsKeyboard;
	private LatinKeyboard mPhoneticSymbolsShiftedKeyboard;
	private LatinKeyboard mPhoneticKeyboard;
	private LatinKeyboard mPhoneticShiftedKeyboard;
	private LatinKeyboard mKaGaPaSymbolsKeyboard;
	private LatinKeyboard mKaGaPaSymbolsShiftedKeyboard;
	private LatinKeyboard mKaGaPaKeyboard;
	private LatinKeyboard mKaGaPaShiftedKeyboard;
	private LatinKeyboard mKannadaInScriptSymbolsKeyboard;
	private LatinKeyboard mKannadaInScriptSymbolsShiftedKeyboard;
	private LatinKeyboard mKannadaInScriptKeyboard;
	private LatinKeyboard mKannadaInScriptShiftedKeyboard;
	private LatinKeyboard mKannada3x4NumbersKeyboard;
	private LatinKeyboard mKannada3x4NumbersShiftedKeyboard;
	private LatinKeyboard mKannada3x4Keyboard;
	private LatinKeyboard mKannada3x4SymbolsKeyboard;
	private LatinKeyboard mHindiRemingtonKeyboard;
	private LatinKeyboard mHindiRemingtonShiftedKeyboard;
	private LatinKeyboard mHindiRemingtonSymbolsKeyboard;
	private LatinKeyboard mHindiRemingtonSymbolsShiftedKeyboard;
	private LatinKeyboard mHindi3x4NumbersKeyboard;
	private LatinKeyboard mHindi3x4NumbersShiftedKeyboard;
	private LatinKeyboard mHindi3x4Keyboard;
	private LatinKeyboard mHindi3x4SymbolsKeyboard;
	private LatinKeyboard mHindiInScriptSymbolsKeyboard;
	private LatinKeyboard mHindiInScriptSymbolsShiftedKeyboard;
	private LatinKeyboard mHindiInScriptKeyboard;
	private LatinKeyboard mHindiInScriptShiftedKeyboard;
	private LatinKeyboard mTamil3x4NumbersKeyboard;
	private LatinKeyboard mTamil3x4NumbersShiftedKeyboard;
	private LatinKeyboard mTamil3x4Keyboard;
	private LatinKeyboard mTamil3x4SymbolsKeyboard;

	private LatinKeyboard getPhoneticKeyboard() {
		if (mPhoneticKeyboard == null) {
			mPhoneticKeyboard = new LatinKeyboard(this, R.xml.phonetic);
		}
		return mPhoneticKeyboard;
	}

	private LatinKeyboard getPhoneticShiftedKeyboard() {
		if (mPhoneticShiftedKeyboard == null) {
			mPhoneticShiftedKeyboard = new LatinKeyboard(this, R.xml.phonetic_shift);
		}
		return mPhoneticShiftedKeyboard;
	}

	private LatinKeyboard getPhoneticSymbolsKeyboard() {
		if (mPhoneticSymbolsKeyboard == null) {
			mPhoneticSymbolsKeyboard = new LatinKeyboard(this, R.xml.phonetic_symbols);
		}
		return mPhoneticSymbolsKeyboard;
	}

	private LatinKeyboard getPhoneticSymbolsShiftedKeyboard() {
		if (mPhoneticSymbolsShiftedKeyboard == null) {
			mPhoneticSymbolsShiftedKeyboard = new LatinKeyboard(this, R.xml.phonetic_symbols_shift);
		}
		return mPhoneticSymbolsShiftedKeyboard;
	}

	private LatinKeyboard getKaGaPaKeyboard() {
		if (mKaGaPaKeyboard == null) {
			mKaGaPaKeyboard = new LatinKeyboard(this, R.xml.kagapa);
		}
		return mKaGaPaKeyboard;
	}

	private LatinKeyboard getKaGaPaShiftedKeyboard() {
		if (mKaGaPaShiftedKeyboard == null) {
			mKaGaPaShiftedKeyboard = new LatinKeyboard(this, R.xml.kagapa_shift);
		}
		return mKaGaPaShiftedKeyboard;
	}

	private LatinKeyboard getKaGaPaSymbolsKeyboard() {
		if (mKaGaPaSymbolsKeyboard == null) {
			mKaGaPaSymbolsKeyboard = new LatinKeyboard(this, R.xml.kagapa_symbols);
		}
		return mKaGaPaSymbolsKeyboard;
	}

	private LatinKeyboard getKaGaPaSymbolsShiftedKeyboard() {
		if (mKaGaPaSymbolsShiftedKeyboard == null) {
			mKaGaPaSymbolsShiftedKeyboard = new LatinKeyboard(this, R.xml.kagapa_symbols_shift);
		}
		return mKaGaPaSymbolsShiftedKeyboard;
	}

	private LatinKeyboard getKannadaInScriptKeyboard() {
		if (mKannadaInScriptKeyboard == null) {
			mKannadaInScriptKeyboard = new LatinKeyboard(this, R.xml.kannada_inscript);
		}
		return mKannadaInScriptKeyboard;
	}

	private LatinKeyboard getKannadaInScriptShiftedKeyboard() {
		if (mKannadaInScriptShiftedKeyboard == null) {
			mKannadaInScriptShiftedKeyboard = new LatinKeyboard(this, R.xml.kannada_inscript_shift);
		}
		return mKannadaInScriptShiftedKeyboard;
	}

	private LatinKeyboard getKannadaInScriptSymbolsKeyboard() {
		if (mKannadaInScriptSymbolsKeyboard == null) {
			mKannadaInScriptSymbolsKeyboard = new LatinKeyboard(this, R.xml.kannada_inscript_symbols);
		}
		return mKannadaInScriptSymbolsKeyboard;
	}

	private LatinKeyboard getKannadaInScriptSymbolsShiftedKeyboard() {
		if (mKannadaInScriptSymbolsShiftedKeyboard == null) {
			mKannadaInScriptSymbolsShiftedKeyboard = new LatinKeyboard(this,
					R.xml.kannada_inscript_symbols_shift);
		}
		return mKannadaInScriptSymbolsShiftedKeyboard;
	}

	private LatinKeyboard getKannada3x4Keyboard() {
		if (mKannada3x4Keyboard == null) {
			mKannada3x4Keyboard = new LatinKeyboard(this, R.xml.kannada_3x4);
		}
		return mKannada3x4Keyboard;
	}

	private LatinKeyboard getKannada3x4NumbersKeyboard() {
		if (mKannada3x4NumbersKeyboard == null) {
			mKannada3x4NumbersKeyboard = new LatinKeyboard(this, R.xml.kannada_3x4_numbers);
		}
		return mKannada3x4NumbersKeyboard;
	}

	private LatinKeyboard getKannada3x4NumbersShiftedKeyboard() {
		if (mKannada3x4NumbersShiftedKeyboard == null) {
			mKannada3x4NumbersShiftedKeyboard = new LatinKeyboard(this, R.xml.kannada_3x4_numbers_shift);
		}
		return mKannada3x4NumbersShiftedKeyboard;
	}

	private LatinKeyboard getKannada3x4SymbolsKeyboard() {
		if (mKannada3x4SymbolsKeyboard == null) {
			mKannada3x4SymbolsKeyboard = new LatinKeyboard(this, R.xml.kannada_3x4_symbols);
		}
		return mKannada3x4SymbolsKeyboard;
	}

	private LatinKeyboard getHindiRemingtonKeyboard() {
		if (mHindiRemingtonKeyboard == null) {
			mHindiRemingtonKeyboard = new LatinKeyboard(this, R.xml.hindi_remington);
		}
		return mHindiRemingtonKeyboard;
	}

	private LatinKeyboard getHindiRemingtonShiftedKeyboard() {
		if (mHindiRemingtonShiftedKeyboard == null) {
			mHindiRemingtonShiftedKeyboard = new LatinKeyboard(this, R.xml.hindi_remington_shift);
		}
		return mHindiRemingtonShiftedKeyboard;
	}

	private LatinKeyboard getHindiRemingtonSymbolsKeyboard() {
		if (mHindiRemingtonSymbolsKeyboard == null) {
			mHindiRemingtonSymbolsKeyboard = new LatinKeyboard(this, R.xml.hindi_remington_symbols);
		}
		return mHindiRemingtonSymbolsKeyboard;
	}

	private LatinKeyboard getHindiRemingtonSymbolsShiftedKeyboard() {
		if (mHindiRemingtonSymbolsShiftedKeyboard == null) {
			mHindiRemingtonSymbolsShiftedKeyboard = new LatinKeyboard(this,
					R.xml.hindi_remington_symbols_shift);
		}
		return mHindiRemingtonSymbolsShiftedKeyboard;
	}

	private LatinKeyboard getHindi3x4Keyboard() {
		if (mHindi3x4Keyboard == null) {
			mHindi3x4Keyboard = new LatinKeyboard(this, R.xml.hindi_3x4);
		}
		return mHindi3x4Keyboard;
	}

	private LatinKeyboard getHindi3x4NumbersKeyboard() {
		if (mHindi3x4NumbersKeyboard == null) {
			mHindi3x4NumbersKeyboard = new LatinKeyboard(this, R.xml.hindi_3x4_numbers);
		}
		return mHindi3x4NumbersKeyboard;
	}

	private LatinKeyboard getHindi3x4NumbersShiftedKeyboard() {
		if (mHindi3x4NumbersShiftedKeyboard == null) {
			mHindi3x4NumbersShiftedKeyboard = new LatinKeyboard(this, R.xml.hindi_3x4_numbers_shift);
		}
		return mHindi3x4NumbersShiftedKeyboard;
	}

	private LatinKeyboard getHindi3x4SymbolsKeyboard() {
		if (mHindi3x4SymbolsKeyboard == null) {
			mHindi3x4SymbolsKeyboard = new LatinKeyboard(this, R.xml.hindi_3x4_symbols);
		}
		return mHindi3x4SymbolsKeyboard;
	}

	private LatinKeyboard getHindiInScriptKeyboard() {
		if (mHindiInScriptKeyboard == null) {
			mHindiInScriptKeyboard = new LatinKeyboard(this, R.xml.hindi_inscript);
		}
		return mHindiInScriptKeyboard;
	}

	private LatinKeyboard getHindiInScriptShiftedKeyboard() {
		if (mHindiInScriptShiftedKeyboard == null) {
			mHindiInScriptShiftedKeyboard = new LatinKeyboard(this, R.xml.hindi_inscript_shift);
		}
		return mHindiInScriptShiftedKeyboard;
	}

	private LatinKeyboard getHindiInScriptSymbolsKeyboard() {
		if (mHindiInScriptSymbolsKeyboard == null) {
			mHindiInScriptSymbolsKeyboard = new LatinKeyboard(this, R.xml.hindi_inscript_symbols);
		}
		return mHindiInScriptSymbolsKeyboard;
	}

	private LatinKeyboard getHindiInScriptSymbolsShiftedKeyboard() {
		if (mHindiInScriptSymbolsShiftedKeyboard == null) {
			mHindiInScriptSymbolsShiftedKeyboard = new LatinKeyboard(this, R.xml.hindi_inscript_symbols_shift);
		}
		return mHindiInScriptSymbolsShiftedKeyboard;
	}

	private LatinKeyboard getTamil3x4Keyboard() {
		if (mTamil3x4Keyboard == null) {
			mTamil3x4Keyboard = new LatinKeyboard(this, R.xml.tamil_3x4);
		}
		return mTamil3x4Keyboard;
	}

	private LatinKeyboard getTamil3x4NumbersKeyboard() {
		if (mTamil3x4NumbersKeyboard == null) {
			mTamil3x4NumbersKeyboard = new LatinKeyboard(this, R.xml.tamil_3x4_numbers);
		}
		return mTamil3x4NumbersKeyboard;
	}

	private LatinKeyboard getTamil3x4NumbersShiftedKeyboard() {
		if (mTamil3x4NumbersShiftedKeyboard == null) {
			mTamil3x4NumbersShiftedKeyboard = new LatinKeyboard(this, R.xml.tamil_3x4_numbers_shift);
		}
		return mTamil3x4NumbersShiftedKeyboard;
	}

	private LatinKeyboard getTamil3x4SymbolsKeyboard() {
		if (mTamil3x4SymbolsKeyboard == null) {
			mTamil3x4SymbolsKeyboard = new LatinKeyboard(this, R.xml.tamil_3x4_symbols);
		}
		return mTamil3x4SymbolsKeyboard;
	}

	private void resetKeyboards() {
		mPhoneticSymbolsKeyboard = null;
		mPhoneticSymbolsShiftedKeyboard = null;
		mPhoneticKeyboard = null;
		mPhoneticShiftedKeyboard = null;
		mKaGaPaSymbolsKeyboard = null;
		mKaGaPaSymbolsShiftedKeyboard = null;
		mKaGaPaKeyboard = null;
		mKaGaPaShiftedKeyboard = null;
		mKannadaInScriptSymbolsKeyboard = null;
		mKannadaInScriptSymbolsShiftedKeyboard = null;
		mKannadaInScriptKeyboard = null;
		mKannadaInScriptShiftedKeyboard = null;
		mKannada3x4NumbersKeyboard = null;
		mKannada3x4NumbersShiftedKeyboard = null;
		mKannada3x4Keyboard = null;
		mKannada3x4SymbolsKeyboard = null;
		mHindiRemingtonKeyboard = null;
		mHindiRemingtonShiftedKeyboard = null;
		mHindiRemingtonSymbolsKeyboard = null;
		mHindiRemingtonSymbolsShiftedKeyboard = null;
		mHindi3x4NumbersKeyboard = null;
		mHindi3x4NumbersShiftedKeyboard = null;
		mHindi3x4Keyboard = null;
		mHindi3x4SymbolsKeyboard = null;
		mHindiInScriptSymbolsKeyboard = null;
		mHindiInScriptSymbolsShiftedKeyboard = null;
		mHindiInScriptKeyboard = null;
		mHindiInScriptShiftedKeyboard = null;
		mTamil3x4NumbersKeyboard = null;
		mTamil3x4NumbersShiftedKeyboard = null;
		mTamil3x4Keyboard = null;
		mTamil3x4SymbolsKeyboard = null;
	}

	static private LatinKeyboard mCurKeyboard;

	private String mWordSeparators;

	static private Set<Integer> mConsonants;
	static private HashMap<Integer, Integer> mVowels;

	static {
		mConsonants = new HashSet<Integer>();
		for (int i = 'ಕ'; i <= 'ಹ'; i++) {
			mConsonants.add(i);
		}

		mVowels = new HashMap<Integer, Integer>();
		for (int i = 'ಆ', j = 'ಾ'; i <= 'ಔ'; i++, j++) {
			mVowels.put(i, j);
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		resetKeyboards();
	}

	/***
	 * Main initialization of the input method component. Be sure to call to super class.
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		mSharedPreferences = getSharedPreferences(KEYBOARD_PREFERENCES, MODE_PRIVATE);
		mWordSeparators = getResources().getString(R.string.word_separators);
	}

	/**
	 * This is the point where you can do all of your UI initialization. It is called after creation and any
	 * configuration change.
	 */
	@Override
	public void onInitializeInterface() {
		if (mPhoneticKeyboard != null) {
			// Configuration changes can happen after the keyboard gets recreated,
			// so we need to be able to re-build the keyboards if the available
			// space has changed.
			int displayWidth = getMaxWidth();
			if (displayWidth == mLastDisplayWidth) {
				return;
			}
			mLastDisplayWidth = displayWidth;
		}
	}

	private String getLayoutPreferenceKey(int language) {
		switch (language) {
		case KB_LANGUAGE_HINDI:
			return KB_CURRENT_HINDI_LAYOUT;
		case KB_LANGUAGE_KANNADA:
			return KB_CURRENT_KANNADA_LAYOUT;
		case KB_LANGUAGE_TAMIL:
			return KB_CURRENT_TAMIL_LAYOUT;
		default:
			return KB_CURRENT_KANNADA_LAYOUT;
		}
	}

	/**
	 * Called by the framework when your view for creating input needs to be generated. This will be called
	 * the first time your input method is displayed, and every time it needs to be re-created such as due to
	 * a configuration change.
	 */
	@Override
	public View onCreateInputView() {
		int keyboardLanguage = mSharedPreferences.getInt(KB_CURRENT_LANGUAGE, KB_DEFAULT_LANGUAGE);
		int keyboardLayout = mSharedPreferences.getInt(getLayoutPreferenceKey(keyboardLanguage),
				KB_DEFAULT_LAYOUT);
		mInputView = (KeyboardView) getLayoutInflater().inflate(R.layout.input, null);
		mInputView.setOnKeyboardActionListener(this);
		mInputView.setKeyboard(getCurrentKeyboard(keyboardLanguage, keyboardLayout));
		return mInputView;
	}

	/**
	 * Called by the framework when your view for showing candidates needs to be generated, like
	 * {@link #onCreateInputView}.
	 */
	@Override
	public View onCreateCandidatesView() {
		mCandidateView = new CandidateView(this);
		mCandidateView.setService(this);
		return mCandidateView;
	}

	/**
	 * This is the main point where we do our initialization of the input method to begin operating on an
	 * application. At this point we have been bound to the client, and are now receiving all of the detailed
	 * information about the target of our edits.
	 */
	@Override
	public void onStartInput(EditorInfo attribute, boolean restarting) {
		super.onStartInput(attribute, restarting);

		int keyboardLanguage = mSharedPreferences.getInt(KB_CURRENT_LANGUAGE, KB_DEFAULT_LANGUAGE);
		int keyboardLayout = mSharedPreferences.getInt(getLayoutPreferenceKey(keyboardLanguage),
				KB_DEFAULT_LAYOUT);
		shiftState = SHIFT_STATE_INITIAL;

		// Reset our state. We want to do this even if restarting, because
		// the underlying state of the text editor could have changed in any way.
		mComposing.setLength(0);
		updateCandidates();

		if (!restarting) {
			// Clear shift states.
			mMetaState = 0;
		}

		mPredictionOn = false;
		mCompletionOn = false;
		mCompletions = null;

		// We are now going to initialize our state based on the type of
		// text being edited.
		switch (attribute.inputType & EditorInfo.TYPE_MASK_CLASS) {
		case EditorInfo.TYPE_CLASS_NUMBER:
		case EditorInfo.TYPE_CLASS_DATETIME:
			// Numbers and dates default to the symbols keyboard, with
			// no extra features.
			mCurKeyboard = getPhoneticSymbolsKeyboard();
			break;

		case EditorInfo.TYPE_CLASS_PHONE:
			// Phones will also default to the symbols keyboard, though
			// often you will want to have a dedicated phone keyboard.
			mCurKeyboard = getPhoneticSymbolsKeyboard();
			break;

		case EditorInfo.TYPE_CLASS_TEXT:
			// This is general text editing. We will default to the
			// normal alphabetic keyboard, and assume that we should
			// be doing predictive text (showing candidates as the
			// user types).
			mCurKeyboard = getCurrentKeyboard(keyboardLanguage, keyboardLayout);
			mPredictionOn = false;

			// We now look for a few special variations of text that will
			// modify our behavior.
			int variation = attribute.inputType & EditorInfo.TYPE_MASK_VARIATION;
			if (variation == EditorInfo.TYPE_TEXT_VARIATION_PASSWORD
					|| variation == EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
				// Do not display predictions / what the user is typing
				// when they are entering a password.
				mPredictionOn = false;
			}

			if (variation == EditorInfo.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
					|| variation == EditorInfo.TYPE_TEXT_VARIATION_URI
					|| variation == EditorInfo.TYPE_TEXT_VARIATION_FILTER) {
				// Our predictions are not useful for e-mail addresses
				// or URIs.
				mPredictionOn = false;
			}

			if ((attribute.inputType & EditorInfo.TYPE_TEXT_FLAG_AUTO_COMPLETE) != 0) {
				// If this is an auto-complete text view, then our predictions
				// will not be shown and instead we will allow the editor
				// to supply their own. We only show the editor's
				// candidates when in fullscreen mode, otherwise relying
				// own it displaying its own UI.
				mPredictionOn = false;
				mCompletionOn = isFullscreenMode();
			}

			// We also want to look at the current state of the editor
			// to decide whether our alphabetic keyboard should start out
			// shifted.
			updateShiftKeyState(attribute);
			break;

		default:
			// For all unknown input types, default to the alphabetic
			// keyboard with no special features.
			mCurKeyboard = getCurrentKeyboard(keyboardLanguage, keyboardLayout);
			updateShiftKeyState(attribute);
		}

		// Update the label on the enter key, depending on what the application
		// says it will do.
		mCurKeyboard.setImeOptions(getResources(), attribute.imeOptions);
	}

	private LatinKeyboard getCurrentKeyboard(int keyboardLanguage, int keyboardLayout) {
		switch (keyboardLanguage) {
		case KB_LANGUAGE_KANNADA:
			switch (keyboardLayout) {
			case KB_KAGAPA:
				return getKaGaPaKeyboard();
			case KB_KANNADA_INSCRIPT:
				return getKannadaInScriptKeyboard();
			case KB_KANNADA_3x4:
				return getKannada3x4Keyboard();
			case KB_KANNADA_PHONETIC:
				return getPhoneticKeyboard();
			}
		case KB_LANGUAGE_HINDI:
			switch (keyboardLayout) {
			case KB_HINDI_REMINGTON:
				return getHindiRemingtonKeyboard();
			case KB_HINDI_INSCRIPT:
				return getHindiInScriptKeyboard();
			case KB_HINDI_3x4:
				return getHindi3x4Keyboard();
			case KB_HINDI_PHONETIC:
				return getPhoneticKeyboard();
			}
		case KB_LANGUAGE_TAMIL:
			switch (keyboardLayout) {
			case KB_TAMIL_3x4:
				return getTamil3x4Keyboard();
			case KB_TAMIL_PHONETIC:
				return getPhoneticKeyboard();
			}
		default:
			return getPhoneticKeyboard();
		}
	}

	/**
	 * This is called when the user is done editing a field. We can use this to reset our state.
	 */
	@Override
	public void onFinishInput() {
		super.onFinishInput();
		// Clear current composing text and candidates.
		mComposing.setLength(0);
		updateCandidates();

		shiftState = SHIFT_STATE_INITIAL;

		// We only hide the candidates window when finishing input on
		// a particular editor, to avoid popping the underlying application
		// up and down if the user is entering text into the bottom of
		// its window.
		setCandidatesViewShown(false);

		int keyboardLanguage = mSharedPreferences.getInt(KB_CURRENT_LANGUAGE, KB_DEFAULT_LANGUAGE);
		int keyboardLayout = mSharedPreferences.getInt(getLayoutPreferenceKey(keyboardLanguage),
				KB_DEFAULT_LAYOUT);
		mCurKeyboard = getCurrentKeyboard(keyboardLanguage, keyboardLayout);
		if (mInputView != null) {
			mInputView.closing();
		}
	}

	@Override
	public void onStartInputView(EditorInfo attribute, boolean restarting) {
		super.onStartInputView(attribute, restarting);
		// Apply the selected keyboard to the input view.
		mInputView.setKeyboard(mCurKeyboard);
		mInputView.closing();
	}

	/**
	 * Deal with the editor reporting movement of its cursor.
	 */
	@Override
	public void onUpdateSelection(int oldSelStart, int oldSelEnd, int newSelStart, int newSelEnd,
			int candidatesStart, int candidatesEnd) {
		super.onUpdateSelection(oldSelStart, oldSelEnd, newSelStart, newSelEnd, candidatesStart,
				candidatesEnd);

		// If the current selection in the text view changes, we should
		// clear whatever candidate text we have.
		if (mComposing.length() > 0 && (newSelStart != candidatesEnd || newSelEnd != candidatesEnd)) {
			mComposing.setLength(0);
			updateCandidates();
			InputConnection ic = getCurrentInputConnection();
			if (ic != null) {
				ic.finishComposingText();
			}
		}
	}

	/**
	 * This tells us about completions that the editor has determined based on the current text in it. We want
	 * to use this in fullscreen mode to show the completions ourself, since the editor can not be seen in
	 * that situation.
	 */
	@Override
	public void onDisplayCompletions(CompletionInfo[] completions) {
		if (mCompletionOn) {
			mCompletions = completions;
			if (completions == null) {
				setSuggestions(null, false, false);
				return;
			}

			List<String> stringList = new ArrayList<String>();
			for (int i = 0; i < (completions != null ? completions.length : 0); i++) {
				CompletionInfo ci = completions[i];
				if (ci != null)
					stringList.add(ci.getText().toString());
			}
			setSuggestions(stringList, true, true);
		}
	}

	/**
	 * This translates incoming hard key events in to edit operations on an InputConnection. It is only needed
	 * when using the PROCESS_HARD_KEYS option.
	 */
	private boolean translateKeyDown(int keyCode, KeyEvent event) {
		mMetaState = MetaKeyKeyListener.handleKeyDown(mMetaState, keyCode, event);
		int c = event.getUnicodeChar(MetaKeyKeyListener.getMetaState(mMetaState));
		mMetaState = MetaKeyKeyListener.adjustMetaAfterKeypress(mMetaState);
		InputConnection ic = getCurrentInputConnection();
		if (c == 0 || ic == null) {
			return false;
		}

		boolean dead = false;

		if ((c & KeyCharacterMap.COMBINING_ACCENT) != 0) {
			dead = true;
			c = c & KeyCharacterMap.COMBINING_ACCENT_MASK;
		}

		if (mComposing.length() > 0) {
			char accent = mComposing.charAt(mComposing.length() - 1);
			int composed = KeyEvent.getDeadChar(accent, c);

			if (composed != 0) {
				c = composed;
				mComposing.setLength(mComposing.length() - 1);
			}
		}

		onKey(c, null);

		return true;
	}

	/**
	 * Use this to monitor key events being delivered to the application. We get first crack at them, and can
	 * either resume them or let them continue to the app.
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			// The InputMethodService already takes care of the back
			// key for us, to dismiss the input method if it is shown.
			// However, our keyboard could be showing a pop-up window
			// that back should dismiss, so we first allow it to do that.
			if (event.getRepeatCount() == 0 && mInputView != null) {
				if (mInputView.handleBack()) {
					return true;
				}
			}
			break;

		case KeyEvent.KEYCODE_DEL:
			// Special handling of the delete key: if we currently are
			// composing text for the user, we want to modify that instead
			// of let the application to the delete itself.
			if (mComposing.length() > 0) {
				onKey(Keyboard.KEYCODE_DELETE, null);
				return true;
			}
			break;

		case KeyEvent.KEYCODE_ENTER:
			// Let the underlying text editor always handle these.
			return false;

		default:
			// For all other keys, if we want to do transformations on
			// text being entered with a hard keyboard, we need to process
			// it and do the appropriate action.
			if (PROCESS_HARD_KEYS) {
				if (keyCode == KeyEvent.KEYCODE_SPACE && (event.getMetaState() & KeyEvent.META_ALT_ON) != 0) {
					// A silly example: in our input method, Alt+Space
					// is a shortcut for 'android' in lower case.
					InputConnection ic = getCurrentInputConnection();
					if (ic != null) {

						// First, tell the editor that it is no longer in the
						// shift state, since we are consuming this.
						ic.clearMetaKeyStates(KeyEvent.META_ALT_ON);
						keyDownUp(KeyEvent.KEYCODE_A);
						keyDownUp(KeyEvent.KEYCODE_N);
						keyDownUp(KeyEvent.KEYCODE_D);
						keyDownUp(KeyEvent.KEYCODE_R);
						keyDownUp(KeyEvent.KEYCODE_O);
						keyDownUp(KeyEvent.KEYCODE_I);
						keyDownUp(KeyEvent.KEYCODE_D);
						// And we consume this event.
						return true;
					}
				}
				if (mPredictionOn && translateKeyDown(keyCode, event)) {
					return true;
				}
			}
		}

		return super.onKeyDown(keyCode, event);
	}

	/**
	 * Use this to monitor key events being delivered to the application. We get first crack at them, and can
	 * either resume them or let them continue to the app.
	 */
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// If we want to do transformations on text being entered with a hard
		// keyboard, we need to process the up events to update the meta key
		// state we are tracking.
		if (PROCESS_HARD_KEYS) {
			if (mPredictionOn) {
				mMetaState = MetaKeyKeyListener.handleKeyUp(mMetaState, keyCode, event);
			}
		}

		return super.onKeyUp(keyCode, event);
	}

	/**
	 * Helper function to commit any text being composed in to the editor.
	 */
	private void commitTyped(InputConnection inputConnection) {
		if (mComposing.length() > 0) {
			inputConnection.commitText(mComposing, mComposing.length());
			mComposing.setLength(0);
			updateCandidates();
		}
	}

	/**
	 * Helper to update the shift state of our keyboard based on the initial editor state.
	 */
	private void updateShiftKeyState(EditorInfo attr) {
		if (attr != null && mInputView != null && mPhoneticKeyboard == mInputView.getKeyboard()) {
			int caps = 0;
			EditorInfo ei = getCurrentInputEditorInfo();
			if (ei != null && ei.inputType != EditorInfo.TYPE_NULL) {
				// caps = getCurrentInputConnection().getCursorCapsMode(attr.inputType);
			}
			// mInputView.setShifted(mCapsLock || caps != 0);
		}
	}

	/**
	 * Helper to determine if a given character code is alphabetic.
	 */
	private boolean isAlphabet(int code) {
		if (Character.isLetter(code)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Helper to send a key down / key up pair to the current editor.
	 */
	private void keyDownUp(int keyEventCode) {
		getCurrentInputConnection().sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, keyEventCode));
		getCurrentInputConnection().sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, keyEventCode));
	}

	/**
	 * Helper to send a character to the editor as raw key events.
	 */
	private void sendKey(int keyCode) {
		switch (keyCode) {
		case '\n':
			keyDownUp(KeyEvent.KEYCODE_ENTER);
			break;
		default:
			if (keyCode >= '0' && keyCode <= '9') {
				keyDownUp(keyCode - '0' + KeyEvent.KEYCODE_0);
			} else {
				getCurrentInputConnection().commitText(String.valueOf((char) keyCode), 1);
			}
			break;
		}
	}

	// Implementation of KeyboardViewListener
	public void onKey(int presentKeycode, int[] keyCodes) {
		if (isWordSeparator(presentKeycode)) {
			// Handle separator
			if (mComposing.length() > 0) {
				commitTyped(getCurrentInputConnection());
			}
			sendKey(presentKeycode);
			updateShiftKeyState(getCurrentInputEditorInfo());
		} else if (presentKeycode == Keyboard.KEYCODE_DELETE) {
			handleBackspace();
		} else if (presentKeycode == Keyboard.KEYCODE_SHIFT) {
			if (shiftState == SHIFT_STATE_INITIAL) {
				shiftState = SHIFT_STATE_INTERMEDIATE;
				handleShift();
				((LatinKeyboard) mInputView.getKeyboard()).setShiftIconToSticky(false);
			} else if (shiftState == SHIFT_STATE_INTERMEDIATE) {
				shiftState = SHIFT_STATE_FINAL;
				((LatinKeyboard) mInputView.getKeyboard()).setShiftIconToSticky(true);
			} else if (shiftState == SHIFT_STATE_FINAL) {
				shiftState = SHIFT_STATE_INITIAL;
				handleShift();
				((LatinKeyboard) mInputView.getKeyboard()).setShiftIconToSticky(false);
			}
		} else if (presentKeycode == SETTINGS_KEYCODE && mInputView != null) {
			showLanguageOptionsMenu();
		} else if (presentKeycode == Keyboard.KEYCODE_CANCEL) {
			handleClose();
			return;
		} else if (presentKeycode == PHONETIC_LETTERS_TO_SYMBOLS_KEYCODE && mInputView != null) {
			Keyboard current = mInputView.getKeyboard();
			if (current == mPhoneticKeyboard || current == mPhoneticShiftedKeyboard) {
				current = getPhoneticSymbolsKeyboard();
			} else {
				current = getPhoneticKeyboard();
			}
			mInputView.setKeyboard(current);
			if (current == mPhoneticSymbolsKeyboard) {
				current.setShifted(false);
			}
		} else if (presentKeycode == KAGAPA_LETTERS_TO_SYMBOLS_KEYCODE && mInputView != null) {
			Keyboard current = mInputView.getKeyboard();
			if (current == mKaGaPaKeyboard || current == mKaGaPaShiftedKeyboard) {
				current = getKaGaPaSymbolsKeyboard();
			} else {
				current = getKaGaPaKeyboard();
			}
			mInputView.setKeyboard(current);
			if (current == mKaGaPaSymbolsKeyboard) {
				current.setShifted(false);
			}
		} else if (presentKeycode == KANNADA_INSCRIPT_LETTERS_TO_SYMBOLS_KEYCODE && mInputView != null) {
			Keyboard current = mInputView.getKeyboard();
			if (current == mKannadaInScriptKeyboard || current == mKannadaInScriptShiftedKeyboard) {
				current = getKannadaInScriptSymbolsKeyboard();
			} else {
				current = getKannadaInScriptKeyboard();
			}
			mInputView.setKeyboard(current);
			if (current == mKannadaInScriptSymbolsKeyboard) {
				current.setShifted(false);
			}
		} else if (presentKeycode == KANNADA_3X4_LETTERS_TO_NUMBERS_KEYCODE && mInputView != null) {
			Keyboard current = mInputView.getKeyboard();
			if (current == mKannada3x4Keyboard || current == mKannada3x4SymbolsKeyboard) {
				current = getKannada3x4NumbersKeyboard();
			} else {
				current = getKannada3x4Keyboard();
			}
			mInputView.setKeyboard(current);
			if (current == mKannada3x4NumbersKeyboard) {
				current.setShifted(false);
			}
		} else if (presentKeycode == KANNADA_3X4_LETTERS_TO_SYMBOLS_KEYCODE && mInputView != null) {
			Keyboard current = mInputView.getKeyboard();
			if (current == mKannada3x4Keyboard || current == mKannada3x4NumbersKeyboard) {
				current = getKannada3x4SymbolsKeyboard();
			} else {
				current = getKannada3x4Keyboard();
			}
			mInputView.setKeyboard(current);
			if (current == mKannada3x4SymbolsKeyboard) {
				current.setShifted(false);
			}
		} else if (presentKeycode == HINDI_REMINGTON_LETTERS_TO_SYMBOLS_KEYCODE && mInputView != null) {
			Keyboard current = mInputView.getKeyboard();
			if (current == mHindiRemingtonKeyboard || current == mHindiRemingtonShiftedKeyboard) {
				current = getHindiRemingtonSymbolsKeyboard();
			} else {
				current = getHindiRemingtonKeyboard();
			}
			mInputView.setKeyboard(current);
			if (current == mHindiRemingtonSymbolsKeyboard) {
				current.setShifted(false);
			}
		} else if (presentKeycode == HINDI_3X4_LETTERS_TO_NUMBERS_KEYCODE && mInputView != null) {
			Keyboard current = mInputView.getKeyboard();
			if (current == mHindi3x4Keyboard || current == mHindi3x4SymbolsKeyboard) {
				current = getHindi3x4NumbersKeyboard();
			} else {
				current = getHindi3x4Keyboard();
			}
			mInputView.setKeyboard(current);
			if (current == mHindi3x4NumbersKeyboard) {
				current.setShifted(false);
			}
		} else if (presentKeycode == HINDI_3X4_LETTERS_TO_SYMBOLS_KEYCODE && mInputView != null) {
			Keyboard current = mInputView.getKeyboard();
			if (current == mHindi3x4Keyboard || current == mHindi3x4NumbersKeyboard) {
				current = getHindi3x4SymbolsKeyboard();
			} else {
				current = getHindi3x4Keyboard();
			}
			mInputView.setKeyboard(current);
			if (current == mHindi3x4SymbolsKeyboard) {
				current.setShifted(false);
			}
		} else if (presentKeycode == HINDI_INSCRIPT_LETTERS_TO_SYMBOLS_KEYCODE && mInputView != null) {
			Keyboard current = mInputView.getKeyboard();
			if (current == mHindiInScriptKeyboard || current == mHindiInScriptShiftedKeyboard) {
				current = getHindiInScriptSymbolsKeyboard();
			} else {
				current = getHindiInScriptKeyboard();
			}
			mInputView.setKeyboard(current);
			if (current == mHindiInScriptSymbolsKeyboard) {
				current.setShifted(false);
			}
		} else if (presentKeycode == TAMIL_3X4_LETTERS_TO_NUMBERS_KEYCODE && mInputView != null) {
			Keyboard current = mInputView.getKeyboard();
			if (current == mTamil3x4Keyboard || current == mTamil3x4SymbolsKeyboard) {
				current = getTamil3x4NumbersKeyboard();
			} else {
				current = getTamil3x4Keyboard();
			}
			mInputView.setKeyboard(current);
			if (current == mTamil3x4NumbersKeyboard) {
				current.setShifted(false);
			}
		} else if (presentKeycode == TAMIL_3X4_LETTERS_TO_SYMBOLS_KEYCODE && mInputView != null) {
			Keyboard current = mInputView.getKeyboard();
			if (current == mTamil3x4Keyboard || current == mTamil3x4NumbersKeyboard) {
				current = getTamil3x4SymbolsKeyboard();
			} else {
				current = getTamil3x4Keyboard();
			}
			mInputView.setKeyboard(current);
			if (current == mTamil3x4SymbolsKeyboard) {
				current.setShifted(false);
			}
		} else {
			handleCharacter(presentKeycode, keyCodes);
		}
		if (shiftState == SHIFT_STATE_INTERMEDIATE && presentKeycode != Keyboard.KEYCODE_SHIFT) {
			shiftState = SHIFT_STATE_INITIAL;
			handleShift();
		}
	}

	private void showKannadaLayoutOptionsMenu() {
		AlertDialog.Builder lyBuilder = new AlertDialog.Builder(this);
		lyBuilder.setCancelable(true);
		lyBuilder.setTitle("Select Layout");
		lyBuilder.setSingleChoiceItems(KANNADA_LAYOUT_CHOICES,
				mSharedPreferences.getInt(KB_CURRENT_KANNADA_LAYOUT, KB_KAGAPA), new OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						SharedPreferences.Editor editor = mSharedPreferences.edit();
						switch (which) {
						case KB_KAGAPA:
							editor.putInt(KB_CURRENT_KANNADA_LAYOUT, KB_KAGAPA);
							mInputView.setKeyboard(getKaGaPaKeyboard());
							break;
						case KB_KANNADA_INSCRIPT:
							editor.putInt(KB_CURRENT_KANNADA_LAYOUT, KB_KANNADA_INSCRIPT);
							mInputView.setKeyboard(getKannadaInScriptKeyboard());
							break;
						case KB_KANNADA_3x4:
							editor.putInt(KB_CURRENT_KANNADA_LAYOUT, KB_KANNADA_3x4);
							mInputView.setKeyboard(getKannada3x4Keyboard());
							break;
						case KB_KANNADA_PHONETIC:
							editor.putInt(KB_CURRENT_KANNADA_LAYOUT, KB_KANNADA_PHONETIC);
							mInputView.setKeyboard(getPhoneticKeyboard());
							break;
						}
						editor.commit();
						dialog.dismiss();
						onFinishInput();
					}
				});
		AlertDialog mOptionsDialog = lyBuilder.create();
		Window window = mOptionsDialog.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();

		lp.token = mInputView.getWindowToken();
		lp.type = WindowManager.LayoutParams.TYPE_APPLICATION_ATTACHED_DIALOG;
		window.setAttributes(lp);
		window.addFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		mOptionsDialog.show();
	}

	private void showHindiLayoutOptionsMenu() {
		AlertDialog.Builder lyBuilder = new AlertDialog.Builder(this);
		lyBuilder.setCancelable(true);
		lyBuilder.setTitle("Select Layout");
		lyBuilder.setSingleChoiceItems(HINDI_LAYOUT_CHOICES,
				mSharedPreferences.getInt(KB_CURRENT_HINDI_LAYOUT, KB_HINDI_INSCRIPT), new OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						SharedPreferences.Editor editor = mSharedPreferences.edit();
						switch (which) {
						case KB_HINDI_REMINGTON:
							editor.putInt(KB_CURRENT_HINDI_LAYOUT, KB_HINDI_REMINGTON);
							mInputView.setKeyboard(getHindiRemingtonKeyboard());
							break;
						case KB_HINDI_INSCRIPT:
							editor.putInt(KB_CURRENT_HINDI_LAYOUT, KB_HINDI_INSCRIPT);
							mInputView.setKeyboard(getHindiInScriptKeyboard());
							break;
						case KB_HINDI_3x4:
							editor.putInt(KB_CURRENT_HINDI_LAYOUT, KB_HINDI_3x4);
							mInputView.setKeyboard(getHindi3x4Keyboard());
							break;
						case KB_HINDI_PHONETIC:
							editor.putInt(KB_CURRENT_HINDI_LAYOUT, KB_HINDI_PHONETIC);
							mInputView.setKeyboard(getPhoneticKeyboard());
							break;
						}
						editor.commit();
						dialog.dismiss();
						onFinishInput();
					}
				});
		AlertDialog mOptionsDialog = lyBuilder.create();
		Window window = mOptionsDialog.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();

		lp.token = mInputView.getWindowToken();
		lp.type = WindowManager.LayoutParams.TYPE_APPLICATION_ATTACHED_DIALOG;
		window.setAttributes(lp);
		window.addFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		mOptionsDialog.show();
	}

	private void showTamilLayoutOptionsMenu() {
		AlertDialog.Builder lyBuilder = new AlertDialog.Builder(this);
		lyBuilder.setCancelable(true);
		lyBuilder.setTitle("Select Layout");
		lyBuilder.setSingleChoiceItems(TAMIL_LAYOUT_CHOICES,
				mSharedPreferences.getInt(KB_CURRENT_TAMIL_LAYOUT, KB_TAMIL_3x4), new OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						SharedPreferences.Editor editor = mSharedPreferences.edit();
						switch (which) {
//						case KB_TAMILNET99:
//							editor.putInt(KB_CURRENT_TAMIL_LAYOUT, KB_TAMILNET99);
//							mInputView.setKeyboard(getTamilnet99Keyboard());
//							break;
//						case KB_TAMIL_REMINGTON:
//							editor.putInt(KB_CURRENT_TAMIL_LAYOUT, KB_TAMIL_REMINGTON);
//							mInputView.setKeyboard(getTamilRemingtonKeyboard());
//							break;
//						case KB_TAMIL_INSCRIPT:
//							editor.putInt(KB_CURRENT_TAMIL_LAYOUT, KB_TAMIL_INSCRIPT);
//							mInputView.setKeyboard(getTamilInScriptKeyboard());
//							break;
						case KB_TAMIL_3x4:
							editor.putInt(KB_CURRENT_TAMIL_LAYOUT, KB_TAMIL_3x4);
							mInputView.setKeyboard(getTamil3x4Keyboard());
							break;
						case KB_TAMIL_PHONETIC:
							editor.putInt(KB_CURRENT_TAMIL_LAYOUT, KB_TAMIL_PHONETIC);
							mInputView.setKeyboard(getPhoneticKeyboard());
							break;
						}
						editor.commit();
						dialog.dismiss();
						onFinishInput();
					}
				});
		AlertDialog mOptionsDialog = lyBuilder.create();
		Window window = mOptionsDialog.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();

		lp.token = mInputView.getWindowToken();
		lp.type = WindowManager.LayoutParams.TYPE_APPLICATION_ATTACHED_DIALOG;
		window.setAttributes(lp);
		window.addFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		mOptionsDialog.show();
	}

	private void showLanguageOptionsMenu() {
		AlertDialog.Builder lanBuilder = new AlertDialog.Builder(this);
		lanBuilder.setCancelable(true);
		lanBuilder.setTitle("Select Language");
		int prevSelectedLanguage = mSharedPreferences.getInt(KB_CURRENT_LANGUAGE, KB_LANGUAGE_KANNADA);
		lanBuilder.setSingleChoiceItems(LANGUAGE_CHOICES, prevSelectedLanguage, new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				SharedPreferences.Editor editor = mSharedPreferences.edit();
				switch (which) {
				case KB_LANGUAGE_HINDI: // Hindi
					Toast.makeText(getBaseContext(), "Hindi Keyboard selected", Toast.LENGTH_SHORT).show();
					editor.putInt(KB_CURRENT_LANGUAGE, KB_LANGUAGE_HINDI);
					showHindiLayoutOptionsMenu();
					break;
				case KB_LANGUAGE_KANNADA: // Kannada
					Toast.makeText(getBaseContext(), "Kannada Keyboard selected", Toast.LENGTH_SHORT).show();
					editor.putInt(KB_CURRENT_LANGUAGE, KB_LANGUAGE_KANNADA);
					showKannadaLayoutOptionsMenu();
					break;
				default:
					editor.putInt(KB_CURRENT_LANGUAGE, KB_LANGUAGE_TAMIL);
					showTamilLayoutOptionsMenu();
					Toast.makeText(getBaseContext(), "Tamil Keyboard selected", Toast.LENGTH_SHORT).show();
				}
				editor.commit();
				dialog.dismiss();
			}
		});
		AlertDialog mOptionsDialog = lanBuilder.create();
		Window window = mOptionsDialog.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();

		lp.token = mInputView.getWindowToken();
		lp.type = WindowManager.LayoutParams.TYPE_APPLICATION_ATTACHED_DIALOG;
		window.setAttributes(lp);
		window.addFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		mOptionsDialog.show();
	}

	public void onText(CharSequence text) {
		InputConnection ic = getCurrentInputConnection();
		if (ic == null)
			return;
		ic.beginBatchEdit();
		if (mComposing.length() > 0) {
			commitTyped(ic);
		}
		ic.commitText(text, 0);
		ic.endBatchEdit();
		updateShiftKeyState(getCurrentInputEditorInfo());
	}

	/**
	 * Update the list of available candidates from the current composing text. This will need to be filled in
	 * by however you are determining candidates.
	 */
	private void updateCandidates() {
		if (!mCompletionOn) {
			if (mComposing.length() > 0) {
				ArrayList<String> list = new ArrayList<String>();
				list.add(mComposing.toString());
				setSuggestions(list, true, true);
			} else {
				setSuggestions(null, false, false);
			}
		}
	}

	public void setSuggestions(List<String> suggestions, boolean completions, boolean typedWordValid) {
		if (suggestions != null && suggestions.size() > 0) {
			setCandidatesViewShown(true);
		} else if (isExtractViewShown()) {
			setCandidatesViewShown(true);
		}
		if (mCandidateView != null) {
			mCandidateView.setSuggestions(suggestions, completions, typedWordValid);
		}
	}

	private void handleBackspace() {
		final int length = mComposing.length();
		if (length > 1) {
			mComposing.delete(length - 1, length);
			getCurrentInputConnection().setComposingText(mComposing, 1);
			updateCandidates();
		} else if (length > 0) {
			mComposing.setLength(0);
			getCurrentInputConnection().commitText("", 0);
			updateCandidates();
		} else {
			keyDownUp(KeyEvent.KEYCODE_DEL);
		}
		updateShiftKeyState(getCurrentInputEditorInfo());
	}

	private void handleShift() {
		if (mInputView == null) {
			return;
		}
		Keyboard currentKeyboard = mInputView.getKeyboard();
		if (currentKeyboard == mPhoneticKeyboard) {
			// Alphabet keyboard
			checkToggleCapsLock();
			mPhoneticKeyboard.setShifted(true);
			mInputView.setKeyboard(getPhoneticShiftedKeyboard());
			mPhoneticShiftedKeyboard.setShifted(true);
			// mInputView.setShifted(mCapsLock || !mInputView.isShifted());
		} else if (currentKeyboard == mPhoneticShiftedKeyboard) {
			mPhoneticShiftedKeyboard.setShifted(false);
			mInputView.setKeyboard(getPhoneticKeyboard());
			mPhoneticKeyboard.setShifted(false);
		} else if (currentKeyboard == mPhoneticSymbolsKeyboard) {
			mPhoneticSymbolsKeyboard.setShifted(true);
			mInputView.setKeyboard(getPhoneticSymbolsShiftedKeyboard());
			mPhoneticSymbolsShiftedKeyboard.setShifted(true);
		} else if (currentKeyboard == mPhoneticSymbolsShiftedKeyboard) {
			mPhoneticSymbolsShiftedKeyboard.setShifted(false);
			mInputView.setKeyboard(getPhoneticSymbolsKeyboard());
			mPhoneticSymbolsKeyboard.setShifted(false);
		} else if (currentKeyboard == mKaGaPaSymbolsKeyboard) {
			mKaGaPaSymbolsKeyboard.setShifted(true);
			mInputView.setKeyboard(getKaGaPaSymbolsShiftedKeyboard());
			mKaGaPaSymbolsShiftedKeyboard.setShifted(true);
		} else if (currentKeyboard == mKaGaPaSymbolsShiftedKeyboard) {
			mKaGaPaSymbolsShiftedKeyboard.setShifted(false);
			mInputView.setKeyboard(getKaGaPaSymbolsKeyboard());
			mKaGaPaSymbolsKeyboard.setShifted(false);
		} else if (currentKeyboard == mKaGaPaKeyboard) {
			mKaGaPaKeyboard.setShifted(true);
			mInputView.setKeyboard(getKaGaPaShiftedKeyboard());
			mKaGaPaShiftedKeyboard.setShifted(true);
		} else if (currentKeyboard == mKaGaPaShiftedKeyboard) {
			mKaGaPaShiftedKeyboard.setShifted(false);
			mInputView.setKeyboard(getKaGaPaKeyboard());
			mKaGaPaKeyboard.setShifted(false);
		} else if (currentKeyboard == mKannadaInScriptSymbolsKeyboard) {
			mKannadaInScriptSymbolsKeyboard.setShifted(true);
			mInputView.setKeyboard(getKannadaInScriptSymbolsShiftedKeyboard());
			mKannadaInScriptSymbolsShiftedKeyboard.setShifted(true);
		} else if (currentKeyboard == mKannadaInScriptSymbolsShiftedKeyboard) {
			mKannadaInScriptSymbolsShiftedKeyboard.setShifted(false);
			mInputView.setKeyboard(getKannadaInScriptSymbolsKeyboard());
			mKannadaInScriptSymbolsKeyboard.setShifted(false);
		} else if (currentKeyboard == mKannadaInScriptKeyboard) {
			mKannadaInScriptKeyboard.setShifted(true);
			mInputView.setKeyboard(getKannadaInScriptShiftedKeyboard());
			mKannadaInScriptShiftedKeyboard.setShifted(true);
		} else if (currentKeyboard == mKannadaInScriptShiftedKeyboard) {
			mKannadaInScriptShiftedKeyboard.setShifted(false);
			mInputView.setKeyboard(getKannadaInScriptKeyboard());
			mKannadaInScriptKeyboard.setShifted(false);
		} else if (currentKeyboard == mKannada3x4NumbersKeyboard) {
			mKannada3x4NumbersKeyboard.setShifted(true);
			mInputView.setKeyboard(getKannada3x4NumbersShiftedKeyboard());
			mKannada3x4NumbersShiftedKeyboard.setShifted(true);
		} else if (currentKeyboard == mKannada3x4NumbersShiftedKeyboard) {
			mKannada3x4NumbersShiftedKeyboard.setShifted(false);
			mInputView.setKeyboard(getKannada3x4NumbersKeyboard());
			mKannada3x4NumbersKeyboard.setShifted(false);
		} else if (currentKeyboard == mHindiRemingtonSymbolsKeyboard) {
			mHindiRemingtonSymbolsKeyboard.setShifted(true);
			mInputView.setKeyboard(getHindiRemingtonSymbolsShiftedKeyboard());
			mHindiRemingtonSymbolsShiftedKeyboard.setShifted(true);
		} else if (currentKeyboard == mHindiRemingtonSymbolsShiftedKeyboard) {
			mHindiRemingtonSymbolsShiftedKeyboard.setShifted(false);
			mInputView.setKeyboard(getHindiRemingtonSymbolsKeyboard());
			mHindiRemingtonSymbolsKeyboard.setShifted(false);
		} else if (currentKeyboard == mHindiRemingtonKeyboard) {
			mHindiRemingtonKeyboard.setShifted(true);
			mInputView.setKeyboard(getHindiRemingtonShiftedKeyboard());
			mHindiRemingtonShiftedKeyboard.setShifted(true);
		} else if (currentKeyboard == mHindiRemingtonShiftedKeyboard) {
			mHindiRemingtonShiftedKeyboard.setShifted(false);
			mInputView.setKeyboard(getHindiRemingtonKeyboard());
			mHindiRemingtonKeyboard.setShifted(false);
		} else if (currentKeyboard == mHindi3x4NumbersKeyboard) {
			mHindi3x4NumbersKeyboard.setShifted(true);
			mInputView.setKeyboard(getHindi3x4NumbersShiftedKeyboard());
			mHindi3x4NumbersShiftedKeyboard.setShifted(true);
		} else if (currentKeyboard == mHindi3x4NumbersShiftedKeyboard) {
			mHindi3x4NumbersShiftedKeyboard.setShifted(false);
			mInputView.setKeyboard(getHindi3x4NumbersKeyboard());
			mHindi3x4NumbersKeyboard.setShifted(false);
		} else if (currentKeyboard == mHindiInScriptSymbolsKeyboard) {
			mHindiInScriptSymbolsKeyboard.setShifted(true);
			mInputView.setKeyboard(getHindiInScriptSymbolsShiftedKeyboard());
			mHindiInScriptSymbolsShiftedKeyboard.setShifted(true);
		} else if (currentKeyboard == mHindiInScriptSymbolsShiftedKeyboard) {
			mHindiInScriptSymbolsShiftedKeyboard.setShifted(false);
			mInputView.setKeyboard(getHindiInScriptSymbolsKeyboard());
			mHindiInScriptSymbolsKeyboard.setShifted(false);
		} else if (currentKeyboard == mHindiInScriptKeyboard) {
			mHindiInScriptKeyboard.setShifted(true);
			mInputView.setKeyboard(getHindiInScriptShiftedKeyboard());
			mHindiInScriptShiftedKeyboard.setShifted(true);
		} else if (currentKeyboard == mHindiInScriptShiftedKeyboard) {
			mHindiInScriptShiftedKeyboard.setShifted(false);
			mInputView.setKeyboard(getHindiInScriptKeyboard());
			mHindiInScriptKeyboard.setShifted(false);
		} else if (currentKeyboard == mTamil3x4NumbersKeyboard) {
			mTamil3x4NumbersKeyboard.setShifted(true);
			mInputView.setKeyboard(getTamil3x4NumbersShiftedKeyboard());
			mTamil3x4NumbersShiftedKeyboard.setShifted(true);
		} else if (currentKeyboard == mTamil3x4NumbersShiftedKeyboard) {
			mTamil3x4NumbersShiftedKeyboard.setShifted(false);
			mInputView.setKeyboard(getTamil3x4NumbersKeyboard());
			mTamil3x4NumbersKeyboard.setShifted(false);
		}
	}

	private boolean isKaGaPaKeyboard() {
		Keyboard current = mInputView.getKeyboard();
		if (current == mKaGaPaKeyboard || current == mKaGaPaShiftedKeyboard) {
			return true;
		}
		return false;
	}

	private boolean isKannada3x4Keyboard() {
		Keyboard current = mInputView.getKeyboard();
		if (current == mKannada3x4Keyboard) {
			return true;
		}
		return false;
	}

	private void handleCharacter(int primaryCode, int[] keyCodes) {
		if (isKaGaPaKeyboard() || isKannada3x4Keyboard()) {
			InputConnection ic = getCurrentInputConnection();
			if (isKannada3x4Keyboard()) {
				// Don't delete this dummy code.
				// Without this the 3x4 keyboard won't work reliably on Samsung Galaxy 5!
				// Need to understand as to why this is happening.
				ic.getTextBeforeCursor(2, 0).toString();
			}
			String previousCodes = ic.getTextBeforeCursor(1, 0).toString();
			if (previousCodes.length() > 0) {
				int previousCode = previousCodes.codePointAt(0);
				if (mConsonants.contains(previousCode) && mVowels.containsKey(primaryCode)) {
					primaryCode = mVowels.get(primaryCode);
				}
			}
		}
		if (isInputViewShown()) {
			if (mInputView.isShifted()) {
				// primaryCode = Character.toUpperCase(primaryCode);
			}
		}
		if (isAlphabet(primaryCode) && mPredictionOn) {
			mComposing.append((char) primaryCode);
			getCurrentInputConnection().setComposingText(mComposing, 1);
			updateShiftKeyState(getCurrentInputEditorInfo());
			updateCandidates();
		} else {
			if (primaryCode == KSHA_COMPOUND_LETTER) {
				getCurrentInputConnection().commitText("ಕ್ಷ", 3);
			} else if (primaryCode == ARKAAOTTU_COMPOUND_LETTER) {
				getCurrentInputConnection().commitText("ರ್", 2);
			} else if (primaryCode == JNYA_COMPOUND_LETTER) {
				getCurrentInputConnection().commitText("ಜ್ಞ", 2);
			} else if (primaryCode == OTTU_R_COMPOUND_LETTER) {
				getCurrentInputConnection().commitText("್ರ", 2);
			} else if (primaryCode == TRA_COMPOUND_LETTER) {
				getCurrentInputConnection().commitText("ತ್ರ", 2);
			} else if (primaryCode == SHRA_COMPOUND_LETTER) {
				getCurrentInputConnection().commitText("ಶ್ರ", 2);
			} else {
				getCurrentInputConnection().commitText(String.valueOf((char) primaryCode), 1);
			}
		}
	}

	private void handleClose() {
		commitTyped(getCurrentInputConnection());
		requestHideSelf(0);
		mInputView.closing();
	}

	private void checkToggleCapsLock() {
		long now = System.currentTimeMillis();
		if (mLastShiftTime + 800 > now) {
			mCapsLock = !mCapsLock;
			mLastShiftTime = 0;
		} else {
			mLastShiftTime = now;
		}
	}

	private String getWordSeparators() {
		return mWordSeparators;
	}

	public boolean isWordSeparator(int code) {
		String separators = getWordSeparators();
		return separators.contains(String.valueOf((char) code));
	}

	public void pickDefaultCandidate() {
		pickSuggestionManually(0);
	}

	public void pickSuggestionManually(int index) {
		if (mCompletionOn && mCompletions != null && index >= 0 && index < mCompletions.length) {
			CompletionInfo ci = mCompletions[index];
			getCurrentInputConnection().commitCompletion(ci);
			if (mCandidateView != null) {
				mCandidateView.clear();
			}
			updateShiftKeyState(getCurrentInputEditorInfo());
		} else if (mComposing.length() > 0) {
			// If we were generating candidate suggestions for the current
			// text, we would commit one of them here. But for this sample,
			// we will just commit the current text.
			commitTyped(getCurrentInputConnection());
		}
	}

	public void swipeRight() {
		if (mCompletionOn) {
			pickDefaultCandidate();
		}
	}

	public void swipeLeft() {
		handleBackspace();
	}

	public void swipeDown() {
		handleClose();
	}

	public void swipeUp() {
	}

	public void onPress(int primaryCode) {
	}

	public void onRelease(int primaryCode) {
	}
}
