package com.sedsoftware.tackle.root

import app.cash.sqldelight.db.SqlDriver
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.russhwolf.settings.Settings
import com.sedsoftware.tackle.database.DatabaseModule
import com.sedsoftware.tackle.database.DatabaseModuleDependencies
import com.sedsoftware.tackle.database.TackleDatabaseDriverFactory
import com.sedsoftware.tackle.network.NetworkModule
import com.sedsoftware.tackle.network.NetworkModuleDependencies
import com.sedsoftware.tackle.root.integration.RootComponentDefault
import com.sedsoftware.tackle.settings.SettingsModule
import com.sedsoftware.tackle.settings.SettingsModuleDependencies
import com.sedsoftware.tackle.settings.SharedSettingsFactory
import com.sedsoftware.tackle.utils.TackleDispatchers
import com.sedsoftware.tackle.utils.TacklePlatformTools
import org.publicvalue.multiplatform.oidc.appsupport.CodeAuthFlowFactory

@Suppress("FunctionName")
fun RootComponentFactory(
    componentContext: ComponentContext,
    platformTools: TacklePlatformTools,
    authFlowFactory: CodeAuthFlowFactory,
    dispatchers: TackleDispatchers,
): RootComponent {
    val databaseModule = DatabaseModule(
        dependencies = object : DatabaseModuleDependencies {
            override val driver: SqlDriver = TackleDatabaseDriverFactory()
        }
    )

    val settingsModule = SettingsModule(
        dependencies = object : SettingsModuleDependencies {
            override val settings: Settings = SharedSettingsFactory()
        }
    )

    val networkModule = NetworkModule(
        dependencies = object : NetworkModuleDependencies {
            override val authFlowFactory: CodeAuthFlowFactory = authFlowFactory

            override val domainProvider: () -> String = {
                settingsModule.settings.domain
            }

            override val tokenProvider: () -> String = {
                settingsModule.settings.token
            }
        }
    )

    return RootComponentDefault(
        componentContext = componentContext,
        storeFactory = DefaultStoreFactory(),
        unauthorizedApi = networkModule.unauthorized,
        authorizedApi = networkModule.authorized,
        oauthApi = networkModule.oauth,
        database = databaseModule.database,
        settings = settingsModule.settings,
        platformTools = platformTools,
        dispatchers = dispatchers,
    )
}
