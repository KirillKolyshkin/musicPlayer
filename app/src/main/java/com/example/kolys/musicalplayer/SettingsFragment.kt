package com.example.kolys.musicalplayer

import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(bundle: Bundle?, s: String?) = setPreferencesFromResource(R.xml.pref, s)

    companion object {

        fun newInstance(): SettingsFragment = SettingsFragment().apply {
            arguments = Bundle().apply {
                //val args = Bundle()
                //val fragment = SettingsFragment()
                //fragment.arguments = Bundle()
                //return fragment
                arguments = Bundle()
            }
        }
    }

}