package com.sedsoftware.tackle.settings

import com.russhwolf.settings.Settings
import com.sedsoftware.tackle.domain.TackleSettings
import com.sedsoftware.tackle.settings.internal.TackleSettingsInternal

interface SettingsModule {
    val settings: TackleSettings
}

interface SettingsModuleDependencies {
    val settings: Settings
}

fun SettingsModule(dependencies: SettingsModuleDependencies): SettingsModule {
    return object : SettingsModule {
        override val settings: TackleSettings by lazy {
            TackleSettingsInternal(dependencies.settings)
        }
    }
}
