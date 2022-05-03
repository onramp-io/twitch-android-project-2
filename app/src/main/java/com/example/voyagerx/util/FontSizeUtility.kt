package com.example.voyagerx.util

import android.content.Context
import android.content.res.Configuration

class FontSizeUtility {

    fun adjustFontScale(context: Context, scale: Float) : Context {
        val configuration: Configuration = context.resources.configuration
        configuration.fontScale = scale

        return context.createConfigurationContext(configuration)
    }
}