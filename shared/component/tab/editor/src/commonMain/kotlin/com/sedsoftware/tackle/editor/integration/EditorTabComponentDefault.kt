package com.sedsoftware.tackle.editor.integration

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.sedsoftware.tackle.editor.EditorTabComponent
import com.sedsoftware.tackle.editor.EditorTabComponent.Model

class EditorTabComponentDefault(
    private val componentContext: ComponentContext,
    private val storeFactory: StoreFactory,
    private val output: (EditorTabComponent.Output) -> Unit,
) : EditorTabComponent, ComponentContext by componentContext {

    override val model: Value<Model> = MutableValue(
        Model(text = "Editor tab component")
    )
}
