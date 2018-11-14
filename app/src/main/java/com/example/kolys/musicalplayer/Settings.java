package com.example.kolys.musicalplayer;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

public class Settings extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        setPreferencesFromResource(R.xml.pref, s);
    }

    public static Settings newInstance() {
        
        Bundle args = new Bundle();
        
        Settings fragment = new Settings();
        fragment.setArguments(args);
        return fragment;
    }

}