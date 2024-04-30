package com.sedsoftware.tackle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.arkivanov.decompose.defaultComponentContext
import com.sedsoftware.tackle.compose.ui.RootContent
import com.sedsoftware.tackle.root.RootComponent
import com.sedsoftware.tackle.root.RootComponentFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root: RootComponent = RootComponentFactory(
            componentContext = defaultComponentContext(),
            context = applicationContext,
            platformTools = PlatformToolsFactory(applicationContext),
            dispatchers = DefaultDispatchers,
        )

        setContent {
            RootContent(component = root)
        }
    }
}
