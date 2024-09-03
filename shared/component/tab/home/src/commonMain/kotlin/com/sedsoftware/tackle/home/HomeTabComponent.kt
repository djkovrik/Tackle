package com.sedsoftware.tackle.home

import com.arkivanov.decompose.value.Value

interface HomeTabComponent {
    val model: Value<Model>

    fun onNewPostClick()

    data class Model(
        val text: String,
    )
}
