package com.sedsoftware.tackle.settings

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings

@Suppress("FunctionName")
fun SharedSettingsFactory(context: Context) : Settings {
    val delegate: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.applicationContext)
    return SharedPreferencesSettings(delegate)
}
