package com.sedsoftware.tackle.root

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackHandlerOwner
import com.sedsoftware.tackle.auth.AuthComponent
import com.sedsoftware.tackle.domain.TackleException
import com.sedsoftware.tackle.editor.EditorComponent
import com.sedsoftware.tackle.main.MainComponent
import com.sedsoftware.tackle.main.alternatetext.AlternateTextComponent
import com.sedsoftware.tackle.main.viewmedia.ViewMediaComponent
import kotlinx.coroutines.flow.Flow

interface RootComponent : BackHandlerOwner {

    val childStack: Value<ChildStack<*, Child>>

    val alternateTextDialog: Value<ChildSlot<*, AlternateTextComponent>>

    val errorMessages: Flow<TackleException>

    fun onBack()

    sealed class Child {
        data class Auth(val component: AuthComponent) : Child()
        data class Main(val component: MainComponent) : Child()
        data class Editor(val component: EditorComponent) : Child()
        data class ViewImage(val component: ViewMediaComponent) : Child()
        data class ViewVideo(val component: ViewMediaComponent) : Child()
    }
}
