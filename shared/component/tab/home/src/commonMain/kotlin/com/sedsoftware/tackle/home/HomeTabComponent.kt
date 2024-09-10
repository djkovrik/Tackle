package com.sedsoftware.tackle.home

import com.arkivanov.decompose.value.Value

interface HomeTabComponent {
    val model: Value<Model>

    fun onNewPostClick()
    fun onScheduledPostsClick()

    data class Model(
        val text: String,
    )
}
