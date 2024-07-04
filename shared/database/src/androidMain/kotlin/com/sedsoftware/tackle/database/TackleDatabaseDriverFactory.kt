package com.sedsoftware.tackle.database

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver

@Suppress("FunctionName")
fun TackleDatabaseDriverFactory(context: Context): SqlDriver =
    AndroidSqliteDriver(
        schema = TackleAppDatabase.Schema,
        context = context,
        name = "TackleDatabase.db",
    )
