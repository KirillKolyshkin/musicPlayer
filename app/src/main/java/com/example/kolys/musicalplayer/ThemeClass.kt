package com.example.kolys.musicalplayer

import android.content.Context
import android.content.SharedPreferences
import android.support.v7.preference.PreferenceManager

object ThemeClass {
    fun getTheme(context: Context): Int {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        val currentTheme = sharedPref.getString("themes", "AppTheme")
        val themeId: Int
        themeId = when (currentTheme) {
            "AppTheme" -> R.style.AppTheme
            "AppTheme2" -> R.style.AppTheme2
            "AppTheme3" -> R.style.AppTheme3
            else -> R.style.AppTheme
        }

        return themeId
    }
}