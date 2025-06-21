package com.sedsoftware.tackle.domain.model.type

sealed class Timeline {
    data object Home : Timeline()
    data class Public(val local: Boolean) : Timeline()
    data class HashTag(val hashtag: String) : Timeline()
    data class List(val listId: String) : Timeline()
}
