package com.sedsoftware.tackle.root

import android.content.Context
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.russhwolf.settings.Settings
import com.sedsoftware.tackle.network.NetworkModule
import com.sedsoftware.tackle.network.NetworkModuleDependencies
import com.sedsoftware.tackle.root.integration.RootComponentDefault
import com.sedsoftware.tackle.settings.SettingsModule
import com.sedsoftware.tackle.settings.SettingsModuleDependencies
import com.sedsoftware.tackle.settings.SharedSettingsFactory
import com.sedsoftware.tackle.utils.TackleDispatchers
import com.sedsoftware.tackle.utils.TacklePlatformTools

@Suppress("FunctionName")
fun RootComponentFactory(
    componentContext: ComponentContext,
    context: Context,
    platformTools: TacklePlatformTools,
    dispatchers: TackleDispatchers,
): RootComponent {
    val networkModule = NetworkModule(
        dependencies = object : NetworkModuleDependencies {}
    )

    val settingsModule = SettingsModule(
        dependencies = object : SettingsModuleDependencies {
            override val settings: Settings = SharedSettingsFactory(context)
        }
    )

    return RootComponentDefault(
        componentContext = componentContext,
        storeFactory = DefaultStoreFactory(),
        unauthorizedApi = networkModule.unauthorized,
        authorizedApi = networkModule.authorized,
        settings = settingsModule.settings,
        platformTools = platformTools,
        dispatchers = dispatchers,
    )
}
