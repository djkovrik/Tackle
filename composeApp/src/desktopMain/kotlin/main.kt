import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.sedsoftware.tackle.compose.App

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "Tackle") {
        App()
    }
}
