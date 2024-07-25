package com.sedsoftware.tackle.domain.model

data class PlatformFileWrapper(
    val name: String,
    val extension: String,
    val path: String,
    val mimeType: String,
    val size: Long,
    val sizeLabel: String,
    val readBytes: suspend () -> ByteArray,
)
