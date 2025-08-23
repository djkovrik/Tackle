import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.window.ComposeUIViewController
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.github.panpf.sketch.PlatformContext
import com.github.panpf.sketch.SingletonSketch
import com.github.panpf.sketch.Sketch
import com.github.panpf.sketch.cache.MemoryCache
import com.github.panpf.sketch.fetch.KtorHttpUriFetcher
import com.github.panpf.sketch.fetch.internal.KtorHttpUriFetcherProvider
import com.github.panpf.sketch.http.KtorStack
import com.sedsoftware.tackle.DefaultDispatchers
import com.sedsoftware.tackle.PlatformToolsFactory
import com.sedsoftware.tackle.compose.theme.TackleTheme
import com.sedsoftware.tackle.compose.ui.CompositionLocalProviders.LocalFileKitDialogSettings
import com.sedsoftware.tackle.compose.ui.RootContent
import com.sedsoftware.tackle.root.RootComponent
import com.sedsoftware.tackle.root.RootComponentFactory
import io.github.vinceglb.filekit.dialogs.FileKitDialogSettings
import io.ktor.client.HttpClient
import org.publicvalue.multiplatform.oidc.appsupport.IosCodeAuthFlowFactory
import platform.UIKit.UIViewController

private val lifecycle: LifecycleRegistry = LifecycleRegistry()
private val dialogSettings: FileKitDialogSettings = createDialogSettings()
private val root: RootComponent = RootComponentFactory(
    componentContext = DefaultComponentContext(lifecycle),
    platformTools = PlatformToolsFactory(),
    authFlowFactory = IosCodeAuthFlowFactory(),
    dispatchers = DefaultDispatchers,
)

@Suppress("FunctionName")
fun MainViewController(): UIViewController {
    SingletonSketch.setSafe {
        Sketch.Builder(PlatformContext.INSTANCE).apply {
            memoryCache(
                MemoryCache.Builder(PlatformContext.INSTANCE)
                    .maxSizePercent(0.5)
                    .build()
            )

            addIgnoreFetcherProvider(KtorHttpUriFetcherProvider::class)
            addComponents {
                val httpClient = HttpClient {}
                val httpStack = KtorStack(httpClient)
                addFetcher(KtorHttpUriFetcher.Factory(httpStack))
            }
        }.build()
    }

    return ComposeUIViewController {
        TackleTheme {
            CompositionLocalProvider(LocalFileKitDialogSettings provides dialogSettings) {
                RootContent(component = root)
            }
        }
    }
}

private fun createDialogSettings(): FileKitDialogSettings {
    return FileKitDialogSettings.createDefault()
}
