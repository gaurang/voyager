package com.app.voyager.Util;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PreferenceHelper {

	private SharedPreferences preferences;

	public PreferenceHelper(Context context) {
		preferences = PreferenceManager.getDefaultSharedPreferences(context);
	}

	public void addArrayList(String key, Set<String> value) {
		Editor editor = preferences.edit();
		editor.putStringSet(key, value);
		editor.commit();

	}
	public List<String> getArrayList(String key) {
		Set<String> set=preferences.getStringSet(key, null);
		List<String> list = new ArrayList<String>(set);
		return list;
	}

	public void addString(String key, String value) {
		Editor editor = preferences.edit();
		editor.putString(key, value);
		editor.commit();
		
	}

	public void addInteger(String key, int value) {
		Editor editor = preferences.edit();
		editor.putInt(key, value);
		editor.commit();
	}
	
	public void addBoolean(String key, boolean value) {
		Editor editor = preferences.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	public void clear() {
		// here you get your preferences by either of two methods
		Editor editor = preferences.edit();
//		boolean loggedIn=getBoolean(ConstantValues.USER_DETAILS.LOGINID);
		editor.clear();
		editor.commit();
	}
	
	public void remove(String key){
		Editor editor = preferences.edit();
		editor.remove(key);
		editor.commit();
	}

	public boolean contains(String key) {
		return preferences.contains(key);
	}

	public String getString(String key) {
		return preferences.getString(key, "");
	}

	public int getInteger(String key) {
		return preferences.getInt(key, 0);
	}
	
	public Boolean getBoolean(String key) {
		return preferences.getBoolean(key, false);
	}
}
