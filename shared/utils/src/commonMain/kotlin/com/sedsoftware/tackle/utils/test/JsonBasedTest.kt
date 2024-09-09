package com.sedsoftware.tackle.utils.test

import kotlinx.serialization.json.Json
import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath
import okio.SYSTEM

abstract class JsonBasedTest {

    protected val json: Json = Json {
        coerceInputValues = true
        ignoreUnknownKeys = true
        useAlternativeNames = false
    }

    protected inline fun <reified R : Any> responseFromFile(path: String): R {
        val filePath: Path = path.toPath()
        val fileStr: String = FileSystem.SYSTEM.read(filePath) {
            readUtf8()
        }

        return json.decodeFromString<R>(fileStr)
    }
}
