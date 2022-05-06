package com.example.voyagerx.ui.fragments.settings

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.preference.*
import com.example.voyagerx.R
import com.example.voyagerx.util.SharedPreferencesManager

class SettingsFragment : PreferenceFragmentCompat() {
    private val SharedPreferencesManager by lazy { SharedPreferencesManager(requireContext()) }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        //load preferences from an XML resource
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        //getting switch preference for background
        val backgroundPreference: SwitchPreferenceCompat? = findPreference("background")
        backgroundPreference?.let { setBackgroundWallpaper(it) }

        //getting list preference for text size **this does not do anything
        val fontSizePreference: ListPreference? = findPreference("font_sizes")
        fontSizePreference?.let { setFontSize(it) }


        //getting switch preference for background **only showing a toast message
        val notificationPreference: SwitchPreferenceCompat? = findPreference("alerts")
        notificationPreference?.let { setNotifications(it) }

    }

    private fun setBackgroundWallpaper(backgroundPref: SwitchPreferenceCompat) {

        backgroundPref.setOnPreferenceChangeListener { _, newValue ->
            if (newValue == true){
                SharedPreferencesManager.setBackgroundWallpaper(newValue as Boolean)
            } else {
                SharedPreferencesManager.setBackgroundWallpaper(newValue as Boolean)
            }
            true
        }
    }

    private fun setFontSize(fontSizePreference: ListPreference) {

        fontSizePreference.setOnPreferenceChangeListener { preference, newValue ->
            if (preference is ListPreference) {
                val index = preference.findIndexOfValue(newValue.toString())
                val entryvalue = preference.entryValues[index]
                Log.i("selected val", " position - $index, entryvalue - $entryvalue ")
                when (entryvalue) {
                    "Large" -> SharedPreferencesManager.setTextInDropdown("Large")
                    "Medium" -> SharedPreferencesManager.setTextInDropdown("Medium")
                    "Small" -> SharedPreferencesManager.setTextInDropdown("Small")
                }
            }
            true
        }
    }

    private fun setNotifications(notificationPreference: SwitchPreferenceCompat) {
        notificationPreference.setOnPreferenceChangeListener {_, newValue ->
            if (newValue == true){
                SharedPreferencesManager.setLaunchNotifications(newValue as Boolean)
                val toast = Toast.makeText(requireContext(), R.string.notified_of_launches, Toast.LENGTH_SHORT)
                toast.show()
            } else {
                SharedPreferencesManager.setLaunchNotifications(newValue as Boolean)
                val toast = Toast.makeText(requireContext(), R.string.not_notified_of_launches, Toast.LENGTH_SHORT)
                toast.show()
            }
            true
        }
    }

}