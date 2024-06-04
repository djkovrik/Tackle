package com.sedsoftware.tackle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.arkivanov.decompose.defaultComponentContext
import com.sedsoftware.tackle.compose.ui.RootContent
import com.sedsoftware.tackle.root.RootComponent
import com.sedsoftware.tackle.root.RootComponentFactory
import com.sedsoftware.tackle.utils.TacklePlatformTools

class MainActivity : ComponentActivity() {
    private var platformTools: TacklePlatformTools? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        platformTools = PlatformToolsFactory(applicationContext)

        val root: RootComponent = RootComponentFactory(
            componentContext = defaultComponentContext(),
            context = applicationContext,
            platformTools = platformTools!!,
            dispatchers = DefaultDispatchers,
        )

        setContent {
            RootContent(component = root)
        }
    }

    override fun onStart() {
        super.onStart()

        val scheme: String = getString(R.string.app_scheme)
        val uri = intent.data ?: return

        if (!uri.toString().startsWith(scheme)) return

        val code = uri.getQueryParameter("code")
        val error = uri.getQueryParameter("error")
        val errorDescription = uri.getQueryParameter("error_description")

        platformTools?.handleOAuthResponse(code, error, errorDescription)
    }
}
