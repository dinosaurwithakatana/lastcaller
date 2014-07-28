package com.dwak.lastcall;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;

public class LastCallPreferences extends PreferenceActivity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setIcon(R.drawable.ic_launcher);
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		setupSimplePreferencesScreen();
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@SuppressWarnings("deprecation")
	private void setupSimplePreferencesScreen() {
		// In the simplified UI, fragments are not used at all and we instead
		// use the older PreferenceActivity APIs.

		// Add 'general' preferences.
		addPreferencesFromResource(R.xml.prefs);

		// Bind the summaries of EditText/List/Dialog/Ringtone preferences to
		// their values. When their values change, their summaries are updated
		// to reflect the new value, per the Android Design guidelines.
		bindPreferenceSummaryToValue(findPreference(LastCallExtension.PREF_DIAL));
        bindPreferenceSummaryToValue(findPreference(LastCallExtension.PREF_DISPLAY_TIME));
	}

	/**
	 * Binds a preference's summary to its value. More specifically, when the preference's value is
	 * changed, its summary (line of text below the preference title) is updated to reflect the
	 * value. The summary is also immediately updated upon calling this method. The exact display
	 * format is dependent on the type of preference.
	 *
	 * @see #sBindPreferenceSummaryToValueListener
	 */
	private static void bindPreferenceSummaryToValue(Preference preference) {
		// Set the listener to watch for value changes.
		preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

		// Trigger the listener immediately with the preference's
		// current value.
		sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
				PreferenceManager
				.getDefaultSharedPreferences(preference.getContext())
				.getBoolean(preference.getKey(), false));
	}

	/**
	 * A preference value change listener that updates the preference's summary to reflect its new
	 * value.
	 */
	private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
		@Override
		public boolean onPreferenceChange(Preference preference, Object value) {
			if (preference instanceof CheckBoxPreference) {
				// For list preferences, look up the correct display value in
				// the preference's 'entries' list.
				Log.v("Pref","Check toggled " + preference.getKey() + " " + value.toString());
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(preference.getContext()).edit();
                editor.putBoolean("pref_dial", (Boolean) value);
				// Set the summary to reflect the new value.
			}
			return true;
		}
	};
}
