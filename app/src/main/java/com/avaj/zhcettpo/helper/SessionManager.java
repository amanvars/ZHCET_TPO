package com.avaj.zhcettpo.helper;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class SessionManager {
	// LogCat tag
	private static String TAG = SessionManager.class.getSimpleName();

	// Shared Preferences
	SharedPreferences pref;

	Editor editor;
	Context _context;

	// Shared pref mode
	int PRIVATE_MODE = 0;

	// Shared preferences file name
	private static final String PREF_NAME = "details";
	
	private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
	private static final String KEY_IS_FBLOGGED_IN = "isFBLoggedIn";
	private static final String ENROL = "sess_enrol";
	private static final String EMAIL = "sess_email";
	private static final String NAME = "sess_name";
	private static final String UID = "sess_uid";
	private static final String FAC = "ses_fac";

	public SessionManager(Context context) {
		this._context = context;
		pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
	}

	public void setLogin(boolean isLoggedIn) {

		editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);

		// commit changes
		editor.commit();

		Log.d(TAG, "User login session modified!");
	}

	public void setFBLogin(boolean isFBLoggedIn) {

		editor.putBoolean(KEY_IS_FBLOGGED_IN, isFBLoggedIn);

		// commit changes
		editor.commit();

		Log.d(TAG, "User login session modified!");
	}
	public void setDetails(String enrol) {

		editor.putString(ENROL, enrol);

		// commit changes
		editor.commit();

		Log.d(TAG, "User login details set!");
	}

	public void setEmail(String email) {

		editor.putString(EMAIL, email);

		// commit changes
		editor.commit();

		Log.d(TAG, "User email details set!" + email);
	}
	public void setName(String name) {

		editor.putString(NAME, name);

		// commit changes
		editor.commit();

		Log.d(TAG, "User name details set!" + name);
	}
	public void setuid(String uid) {

		editor.putString(UID, uid);

		// commit changes
		editor.commit();

		Log.d(TAG, "User name details set!" + uid);
	}

	public boolean isLoggedIn(){
		return pref.getBoolean(KEY_IS_LOGGED_IN, false);
	}
	public boolean isFBLoggedIn(){
		return pref.getBoolean(KEY_IS_FBLOGGED_IN, false);
	}

	public String session_enrol(){
		return pref.getString(ENROL, "");
	}
	public String session_email(){
		Log.v(TAG,pref.getString(EMAIL, ""));
		return pref.getString(EMAIL, "");

	}
	public String session_name(){
		Log.v(TAG,pref.getString(NAME, ""));
		return pref.getString(NAME, "");

	}
	public String session_uid(){
		Log.v(TAG,pref.getString(UID, ""));
		return pref.getString(UID, "");

	}
}
