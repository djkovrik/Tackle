package com.sedsoftware.tackle.domain.model.type

sealed class Timeline {
    data object Home : Timeline()
    data object Public : Timeline()
    data class HashTag(val hashtag: String) : Timeline()
    data class List(val listId: String) : Timeline()
}
