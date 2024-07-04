package com.sedsoftware.tackle.database

import app.cash.sqldelight.db.SqlDriver
import com.sedsoftware.tackle.database.internal.TackleSharedDatabase
import com.sedsoftware.tackle.database.internal.TackleTestDatabase
import com.sedsoftware.tackle.domain.api.TackleDatabase
import kotlin.coroutines.CoroutineContext

interface DatabaseModule {
    val database: TackleDatabase
    val databaseMock: TackleDatabase
}

interface DatabaseModuleDependencies {
    val driver: SqlDriver
    val coroutineContext: CoroutineContext
    val domainProvider: () -> String
}

fun DatabaseModule(dependencies: DatabaseModuleDependencies): DatabaseModule {
    return object : DatabaseModule {
        override val database: TackleDatabase by lazy {
            TackleSharedDatabase(
                currentDomainProvider = dependencies.domainProvider,
                coroutineContext = dependencies.coroutineContext,
                driver = dependencies.driver,
            )
        }
        override val databaseMock: TackleDatabase by lazy {
            TackleTestDatabase()
        }
    }
}
