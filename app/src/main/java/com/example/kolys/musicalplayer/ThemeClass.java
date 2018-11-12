package com.example.kolys.musicalplayer;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

public class ThemeClass {
    public static int getTheme(Context context) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        String currentTheme = sharedPref.getString("themes", "AppTheme");
        int themeId;
        switch (currentTheme) {
            case "AppTheme":
                themeId = R.style.AppTheme;
                break;
            case "AppTheme2":
                themeId = R.style.AppTheme2;
                break;
            case "AppTheme3":
                themeId = R.style.AppTheme3;
                break;
            default:
                themeId = R.style.AppTheme;
        }

        return themeId;
    }
}
