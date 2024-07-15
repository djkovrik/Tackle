package com.sedsoftware.tackle.settings

import com.russhwolf.settings.PreferencesSettings
import com.russhwolf.settings.Settings
import java.util.prefs.Preferences

@Suppress("FunctionName")
fun SharedSettingsFactory(): Settings {
    val delegate: Preferences = Preferences.userRoot()
    return PreferencesSettings(delegate)
}
