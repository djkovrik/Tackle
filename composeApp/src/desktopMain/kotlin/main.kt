import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
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
import com.sedsoftware.tackle.compose.ui.RootContent
import com.sedsoftware.tackle.root.RootComponentFactory
import com.sedsoftware.tackle.runOnUiThread
import io.ktor.client.HttpClient
import org.publicvalue.multiplatform.oidc.appsupport.JvmCodeAuthFlowFactory

fun main() {
    val lifecycle = LifecycleRegistry()
    val root = runOnUiThread {
        RootComponentFactory(
            componentContext = DefaultComponentContext(lifecycle = lifecycle),
            platformTools = PlatformToolsFactory(),
            authFlowFactory = JvmCodeAuthFlowFactory(),
            dispatchers = DefaultDispatchers,
        )
    }

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

    application {
        Window(onCloseRequest = ::exitApplication, title = "Tackle") {
            TackleTheme {
                RootContent(component = root)
            }
        }
    }
}
