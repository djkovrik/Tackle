package com.sedsoftware.tackle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import com.arkivanov.decompose.defaultComponentContext
import com.sedsoftware.tackle.compose.theme.TackleTheme
import com.sedsoftware.tackle.compose.ui.CompositionLocalProviders.LocalFileKitDialogSettings
import com.sedsoftware.tackle.compose.ui.RootContent
import com.sedsoftware.tackle.root.RootComponent
import com.sedsoftware.tackle.root.RootComponentFactory
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.dialogs.FileKitDialogSettings
import io.github.vinceglb.filekit.dialogs.init
import org.publicvalue.multiplatform.oidc.appsupport.AndroidCodeAuthFlowFactory

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FileKit.init(this)

        val authFlowFactory = AndroidCodeAuthFlowFactory(useWebView = false)
        val dialogSettings = createDialogSettings()

        val root: RootComponent = RootComponentFactory(
            componentContext = defaultComponentContext(),
            context = applicationContext,
            platformTools = PlatformToolsFactory(applicationContext),
            authFlowFactory = authFlowFactory,
            dispatchers = DefaultDispatchers,
        )

        authFlowFactory.registerActivity(this)

        setContent {
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
}
