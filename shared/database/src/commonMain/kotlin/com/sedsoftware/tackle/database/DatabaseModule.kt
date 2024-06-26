package com.sedsoftware.tackle.database

import app.cash.sqldelight.db.SqlDriver
import com.sedsoftware.tackle.database.api.TackleDatabase
import com.sedsoftware.tackle.database.internal.TackleSharedDatabase
import com.sedsoftware.tackle.database.internal.TackleTestDatabase

interface DatabaseModule {
    val database: TackleDatabase
    val databaseMock: TackleDatabase
}

interface DatabaseModuleDependencies {
    val driver: SqlDriver
}

fun DatabaseModule(dependencies: DatabaseModuleDependencies): DatabaseModule {
    return object : DatabaseModule {
        override val database: TackleDatabase by lazy {
            TackleSharedDatabase(dependencies.driver)
        }
        override val databaseMock: TackleDatabase by lazy {
            TackleTestDatabase()
        }
    }
}
