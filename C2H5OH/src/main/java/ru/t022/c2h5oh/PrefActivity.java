package ru.t022.c2h5oh;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.widget.Toast;

public class PrefActivity extends PreferenceActivity  implements OnSharedPreferenceChangeListener  {

	 @Override
	  protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    addPreferencesFromResource(R.xml.settings);
 	}
 
	 public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
		 
	 }

	 @Override
	 protected void onResume() {
	     super.onResume();
	     getPreferenceScreen()
	             .getSharedPreferences()
	             .registerOnSharedPreferenceChangeListener(this);
	 }
	  
	 @Override
	 protected void onPause() {
	     super.onPause();
	     getPreferenceScreen()
	             .getSharedPreferences()
	             .unregisterOnSharedPreferenceChangeListener(this);
	 }
	 
	public PrefActivity() {
		// 
	}
}
