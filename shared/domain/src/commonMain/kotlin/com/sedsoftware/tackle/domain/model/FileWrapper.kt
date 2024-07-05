package com.sedsoftware.tackle.domain.model

class FileWrapper(
    val name: String,
    val extension: String,
    val mimeType: String,
    val data: ByteArray,
)
