package com.sedsoftware.tackle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.arkivanov.decompose.defaultComponentContext
import com.sedsoftware.tackle.compose.theme.TackleTheme
import com.sedsoftware.tackle.compose.ui.RootContent
import com.sedsoftware.tackle.root.RootComponent
import com.sedsoftware.tackle.root.RootComponentFactory
import org.publicvalue.multiplatform.oidc.appsupport.AndroidCodeAuthFlowFactory

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val authFlowFactory = AndroidCodeAuthFlowFactory(useWebView = false)

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
                RootContent(component = root)
            }
        }
    }
}
