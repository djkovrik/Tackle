package com.sedsoftware.tackle.settings

import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.Settings
import platform.Foundation.NSUserDefaults

@Suppress("FunctionName")
fun SharedSettingsFactory() : Settings {
    val delegate: NSUserDefaults = NSUserDefaults.standardUserDefaults()
    return NSUserDefaultsSettings(delegate)
}
