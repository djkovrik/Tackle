package com.sedsoftware.tackle.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver

@Suppress("FunctionName")
fun TackleDatabaseDriverFactory(): SqlDriver =
    NativeSqliteDriver(
        schema = TackleAppDatabase.Schema,
        name = "NexusDatabase.db"
    )
