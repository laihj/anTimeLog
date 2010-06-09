package net.laihj.anTimeLog;

import android.app.Activity;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;

public class settings extends PreferenceActivity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
	addPreferencesFromResource(R.xml.settings);
    }
}
