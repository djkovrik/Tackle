package com.sedsoftware.tackle.root

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.sedsoftware.tackle.auth.AuthComponent
import com.sedsoftware.tackle.domain.TackleException
import com.sedsoftware.tackle.editor.EditorComponent
import com.sedsoftware.tackle.main.MainComponent
import com.sedsoftware.tackle.main.alternatetext.AlternateTextComponent
import kotlinx.coroutines.flow.Flow

interface RootComponent {

    val childStack: Value<ChildStack<*, Child>>

    val alternateTextDialog: Value<ChildSlot<*, AlternateTextComponent>>

    val errorMessages: Flow<TackleException>

    sealed class Child {
        data class Auth(val component: AuthComponent) : Child()
        data class Main(val component: MainComponent) : Child()
        data class Editor(val component: EditorComponent) : Child()
    }
}
