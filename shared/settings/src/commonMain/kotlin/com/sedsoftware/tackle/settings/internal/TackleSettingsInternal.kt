package com.sedsoftware.tackle.settings.internal

import com.russhwolf.settings.Settings
import com.sedsoftware.tackle.settings.TackleSettings

internal class TackleSettingsInternal(
    private val settings: Settings,
) : TackleSettings {

    override var testOption: String
        get() = settings.getValue(PREF_KEY_TEST, "")
        set(value) {
            settings.setValue(PREF_KEY_TEST, value)
        }

    private fun Settings.setValue(key: String, value: Any) {
        when (value) {
            is String -> putString(key, value)
            is Int -> putInt(key, value)
            is Boolean -> putBoolean(key, value)
            is Float -> putFloat(key, value)
            is Long -> putLong(key, value)
            else -> throw UnsupportedOperationException("Not implemented yet")
        }
    }

    private inline fun <reified T : Any> Settings.getValue(key: String, defaultValue: T? = null): T =
        when (T::class) {
            String::class -> getString(key, defaultValue as? String ?: "") as T
            Int::class -> getInt(key, defaultValue as? Int ?: -1) as T
            Boolean::class -> getBoolean(key, defaultValue as? Boolean ?: false) as T
            Float::class -> getFloat(key, defaultValue as? Float ?: -1f) as T
            Long::class -> getLong(key, defaultValue as? Long ?: -1L) as T
            else -> throw UnsupportedOperationException("Not implemented yet")
        }

    private companion object {
        const val PREF_KEY_TEST = "pref_test"
    }
}
