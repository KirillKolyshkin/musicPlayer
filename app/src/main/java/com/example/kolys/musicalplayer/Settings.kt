package com.example.kolys.musicalplayer

import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat

class Settings : PreferenceFragmentCompat() {

    override fun onCreatePreferences(bundle: Bundle?, s: String?) = setPreferencesFromResource(R.xml.pref, s)

    companion object {

        fun newInstance(): Settings {

            val args = Bundle()

            val fragment = Settings()
            fragment.arguments = args
            return fragment
        }
    }

}