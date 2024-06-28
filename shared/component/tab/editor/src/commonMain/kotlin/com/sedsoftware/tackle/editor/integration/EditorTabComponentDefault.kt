package com.sedsoftware.tackle.editor.integration

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.sedsoftware.tackle.domain.TackleDispatchers
import com.sedsoftware.tackle.editor.EditorTabComponent
import com.sedsoftware.tackle.editor.EditorTabComponentGateways
import com.sedsoftware.tackle.editor.header.EditorHeaderComponent
import com.sedsoftware.tackle.editor.header.integration.EditorHeaderComponentDefault
import com.sedsoftware.tackle.editor.integration.header.EditorHeaderSettings
import com.sedsoftware.tackle.editor.integration.header.EditorHeaderTools

class EditorTabComponentDefault(
    private val componentContext: ComponentContext,
    private val storeFactory: StoreFactory,
    private val api: EditorTabComponentGateways.Api,
    private val database: EditorTabComponentGateways.Database,
    private val settings: EditorTabComponentGateways.Settings,
    private val tools: EditorTabComponentGateways.Tools,
    private val dispatchers: TackleDispatchers,
    private val output: (EditorTabComponent.Output) -> Unit,
) : EditorTabComponent, ComponentContext by componentContext {

    override val header: EditorHeaderComponent =
        EditorHeaderComponentDefault(
            componentContext = childContext(
                key = "Editor header",
                lifecycle = lifecycle
            ),
            storeFactory = storeFactory,
            settings = EditorHeaderSettings(settings),
            tools = EditorHeaderTools(tools),
            dispatchers = dispatchers,
            output = ::onEditorHeaderComponentOutput
        )

    private fun onEditorHeaderComponentOutput(output: EditorHeaderComponent.Output) {
        when (output) {
            is EditorHeaderComponent.Output.ErrorCaught -> output(EditorTabComponent.Output.ErrorCaught(output.throwable))
        }
    }
}
