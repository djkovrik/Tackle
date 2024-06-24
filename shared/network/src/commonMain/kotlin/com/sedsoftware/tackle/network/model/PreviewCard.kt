package com.sedsoftware.tackle.network.model

import com.sedsoftware.tackle.network.model.type.PreviewCardType

data class PreviewCard(
    val url: String,
    val title: String,
    val description: String,
    val type: PreviewCardType,
    val authorName: String,
    val authorUrl: String,
    val providerName: String,
    val providerUrl: String,
    val html: String,
    val width: Int,
    val height: Int,
    val image: String,
    val embedUrl: String,
    val blurhash: String,
)
