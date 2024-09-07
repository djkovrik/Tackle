package com.sedsoftware.tackle.editor.details.model

import kotlinx.serialization.Serializable

@Serializable
data class AttachmentImageParams(
    val width: Int,
    val height: Int,
    val ratio: Float,
    val blurhash: String,
) {
    companion object {
        fun empty(): AttachmentImageParams = AttachmentImageParams(0, 0, 0f, "")
    }
}
