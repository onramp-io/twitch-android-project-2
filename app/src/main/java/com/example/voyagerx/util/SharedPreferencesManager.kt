/*

package com.example.voyagerx.util

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesManager(val preference: String) {
    val switch_appearance: String = "switch_appearance"

    private fun getSharedPreferences(context: Context) : SharedPreferences {
        return context.getSharedPreferences(
            "sharedPrefs", Context.MODE_PRIVATE)
    }

    fun getBackgroundImage(context: Context): String? {
        return getSharedPreferences(context).getString(
            "sharedPrefs".switch_appearance, null
        )
    }

    fun getTextSize(context: Context): String? {
        return getSharedPreferences(context).getString(
            "sharedPrefs".textSize, null
        )
    }

    fun getNotifications(context: Context): String? {
        return getSharedPreferences(context).getString(
            "sharedPrefs".switchNotifyValue, null
        )
    }

    fun saveBackgroundImage(context: Context, name: String) {

    }
}
*/
