package com.example.voyagerx.util

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SharedPreferencesManager(val context: Context) {
    private val sharedPref: SharedPreferences =
        context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
    private val editor = sharedPref.edit()

    private fun String.put(long: Long) {
        editor.putLong(this, long)
        editor.apply()
    }

    private fun String.put(int: Int) {
        editor.putInt(this, int)
        editor.apply()
    }

    private fun String.put(string: String) {
        editor.putString(this, string)
        editor.apply()
    }

    private fun String.put(boolean: Boolean) {
        editor.putBoolean(this, boolean)
        editor.apply()
    }

    private fun String.getLong() = sharedPref.getLong(this, 0)
    private fun String.getInt() = sharedPref.getInt(this, 0)
    private fun String.getString() = sharedPref.getString(this, "")!!
    private fun String.getBoolean() = sharedPref.getBoolean(this, true)

    fun setBackgroundWallpaper(wallpaperSelection: Boolean) =
        PREF_BKG_APPEARANCE.put(wallpaperSelection)

    fun getBackgroundWallpaper(): Boolean {
        PREF_BKG_APPEARANCE.getBoolean()
        Log.d("appearance switch", PREF_BKG_APPEARANCE.getBoolean().toString())
        return PREF_BKG_APPEARANCE.getBoolean()
    }

    fun setTextSize(fontSize: String) = PREF_FONT_SIZE.put(fontSize)

    fun getFontSize() : String {
        PREF_FONT_SIZE.getString()
        Log.d("font size value", PREF_FONT_SIZE.getString())
        return PREF_FONT_SIZE.getString()
    }

    fun setLaunchNotifications(notificationSelection: Boolean) =
        PREF_LAUNCH_NOTIFICATIONS.put(notificationSelection)

    fun getLaunchNotifications(): Boolean {
        PREF_LAUNCH_NOTIFICATIONS.getBoolean()
        Log.d("notification switch", PREF_LAUNCH_NOTIFICATIONS.getBoolean().toString())
        return PREF_LAUNCH_NOTIFICATIONS.getBoolean()
    }

    fun setListingFilterSetting(
        isAnyFilter: Boolean,
        value: MutableMap<String, MutableList<String>>,
    ) {
        val json = Gson().toJson(value)
        if (isAnyFilter) {
            PREF_LAUNCH_FILTER_ANY.put(json)
        } else {
            PREF_LAUNCH_FILTER_ALL.put(json)
        }
    }

    fun getListingFilterSetting(isAnyFilter: Boolean): MutableMap<String, MutableList<String>>? {
        val json =
            if (isAnyFilter) PREF_LAUNCH_FILTER_ANY.getString() else PREF_LAUNCH_FILTER_ALL.getString()
        if (json.isNotBlank()) {
            return Gson().fromJson(json,
                object : TypeToken<MutableMap<String, MutableList<String>>>() {}.type)
        }
        return null
    }

    companion object {
        const val PREFERENCE_NAME = "SHARED_PREFS"
        const val PREF_BKG_APPEARANCE = "PREF_BKG_APPEARANCE"
        const val PREF_FONT_SIZE = "PREF_FONT_SIZE"
        const val PREF_LAUNCH_NOTIFICATIONS = "PREF_LAUNCH_NOTIFICATIONS"
        const val PREF_LAUNCH_FILTER_ANY = "PREF_LAUNCH_FILTER_ANY"
        const val PREF_LAUNCH_FILTER_ALL = "PREF_LAUNCH_FILTER_ALL"
    }

}


