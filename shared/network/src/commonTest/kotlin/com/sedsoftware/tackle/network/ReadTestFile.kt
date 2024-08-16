package com.sedsoftware.tackle.network

import kotlinx.serialization.json.Json
import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath
import okio.SYSTEM

private val json: Json = Json {
    coerceInputValues = true
    ignoreUnknownKeys = true
    useAlternativeNames = false
}

internal inline fun <reified R : Any> responseFromFile(path: String): R {
    val filePath: Path = path.toPath()
    val fileStr: String = FileSystem.SYSTEM.read(filePath) {
        readUtf8()
    }

    return json.decodeFromString<R>(fileStr)
}
