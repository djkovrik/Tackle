import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.mvikotlin.core.utils.setMainThreadId
import com.sedsoftware.tackle.DefaultDispatchers
import com.sedsoftware.tackle.compose.ui.RootContent
import com.sedsoftware.tackle.root.RootComponent
import com.sedsoftware.tackle.root.RootComponentFactory
import javax.swing.SwingUtilities

val lifecycle = LifecycleRegistry()
private val root = RootComponentFactory(
    componentContext = DefaultComponentContext(lifecycle = lifecycle),
    dispatchers = DefaultDispatchers,
)

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "Tackle") {
        RootContent(component = root)
    }
}
