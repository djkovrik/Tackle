package com.sedsoftware.tackle.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import java.io.File

@Suppress("FunctionName")
fun TackleDatabaseDriverFactory(): SqlDriver {
    val databasePath = File(System.getProperty("java.io.tmpdir"), "TackleDatabase.db")
    val driver = JdbcSqliteDriver(url = "jdbc:sqlite:${databasePath.absolutePath}")
    TackleAppDatabase.Schema.create(driver)
    return driver
}
