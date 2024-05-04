import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.sedsoftware.tackle.DefaultDispatchers
import com.sedsoftware.tackle.PlatformToolsFactory
import com.sedsoftware.tackle.compose.ui.RootContent
import com.sedsoftware.tackle.root.RootComponentFactory
import com.sedsoftware.tackle.runOnUiThread

fun main() {
    val lifecycle = LifecycleRegistry()
    val root = runOnUiThread {
        RootComponentFactory(
            componentContext = DefaultComponentContext(lifecycle = lifecycle),
            platformTools = PlatformToolsFactory(),
            dispatchers = DefaultDispatchers,
        )
    }

    application {
        Window(onCloseRequest = ::exitApplication, title = "Tackle") {
            RootContent(component = root)
        }
    }
}
