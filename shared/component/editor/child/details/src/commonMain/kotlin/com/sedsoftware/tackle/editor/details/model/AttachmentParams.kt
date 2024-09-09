package com.sedsoftware.tackle.editor.details.model

import kotlinx.serialization.Serializable

@Serializable
data class AttachmentParams(
    val width: Int,
    val height: Int,
    val ratio: Float,
    val blurhash: String,
) {
    companion object {
        fun empty(): AttachmentParams = AttachmentParams(0, 0, 0f, "")
    }
}
