import androidx.compose.ui.window.ComposeUIViewController
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.sedsoftware.tackle.DefaultDispatchers
import com.sedsoftware.tackle.PlatformToolsFactory
import com.sedsoftware.tackle.compose.ui.RootContent
import com.sedsoftware.tackle.root.RootComponent
import com.sedsoftware.tackle.root.RootComponentFactory
import org.publicvalue.multiplatform.oidc.appsupport.IosCodeAuthFlowFactory
import platform.UIKit.UIViewController

private val lifecycle: LifecycleRegistry = LifecycleRegistry()
private val root: RootComponent = RootComponentFactory(
    componentContext = DefaultComponentContext(lifecycle),
    platformTools = PlatformToolsFactory(),
    authFlowFactory = IosCodeAuthFlowFactory(),
    dispatchers = DefaultDispatchers,
)

@Suppress("FunctionName")
fun MainViewController(): UIViewController =
    ComposeUIViewController {
        RootContent(component = root)
    }
