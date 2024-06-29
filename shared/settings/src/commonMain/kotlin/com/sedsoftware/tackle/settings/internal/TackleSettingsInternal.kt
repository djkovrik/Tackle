package com.sedsoftware.tackle.settings.internal

import com.russhwolf.settings.Settings
import com.sedsoftware.tackle.domain.api.TackleSettings

internal class TackleSettingsInternal(
    private val settings: Settings,
) : TackleSettings {

    override var domain: String
        get() = settings.getValue(PREF_KEY_DOMAIN, "")
        set(value) {
            settings.setValue(PREF_KEY_DOMAIN, value)
        }

    override var clientId: String
        get() = settings.getValue(PREF_KEY_CLIENT_ID, "")
        set(value) {
            settings.setValue(PREF_KEY_CLIENT_ID, value)
        }

    override var clientSecret: String
        get() = settings.getValue(PREF_KEY_CLIENT_SECRET, "")
        set(value) {
            settings.setValue(PREF_KEY_CLIENT_SECRET, value)
        }

    override var token: String
        get() = settings.getValue(PREF_KEY_TOKEN, "")
        set(value) {
            settings.setValue(PREF_KEY_TOKEN, value)
        }

    override var ownAvatar: String
        get() = settings.getValue(PREF_KEY_AVATAR, "")
        set(value) {
            settings.setValue(PREF_KEY_AVATAR, value)
        }

    override var ownUsername: String
        get() = settings.getValue(PREF_KEY_USERNAME, "")
        set(value) {
            settings.setValue(PREF_KEY_USERNAME, value)
        }

    override var emojiLastCachedTimestamp: String
        get() = settings.getValue(PREF_KEY_EMOJI_TIMESTAMP, "")
        set(value) {
            settings.setValue(PREF_KEY_EMOJI_TIMESTAMP, value)
        }

    override var lastSelectedLanguageName: String
        get() = settings.getValue(PREF_KEY_LAST_SELECTED_LANGUAGE_NAME, "")
        set(value) {
            settings.setValue(PREF_KEY_LAST_SELECTED_LANGUAGE_NAME, value)
        }

    override var lastSelectedLanguageCode: String
        get() = settings.getValue(PREF_KEY_LAST_SELECTED_LANGUAGE_CODE, "")
        set(value) {
            settings.setValue(PREF_KEY_LAST_SELECTED_LANGUAGE_CODE, value)
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
        const val PREF_KEY_DOMAIN = "dn"
        const val PREF_KEY_CLIENT_ID = "ci"
        const val PREF_KEY_CLIENT_SECRET = "cs"
        const val PREF_KEY_TOKEN = "tn"
        const val PREF_KEY_AVATAR = "av"
        const val PREF_KEY_USERNAME = "un"
        const val PREF_KEY_EMOJI_TIMESTAMP = "et"
        const val PREF_KEY_LAST_SELECTED_LANGUAGE_NAME = "lsln"
        const val PREF_KEY_LAST_SELECTED_LANGUAGE_CODE = "lslc"
    }
}
