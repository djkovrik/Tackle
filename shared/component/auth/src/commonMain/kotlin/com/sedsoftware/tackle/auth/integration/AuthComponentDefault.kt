package com.sedsoftware.tackle.auth.integration

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.sedsoftware.tackle.auth.AuthComponent
import com.sedsoftware.tackle.auth.AuthComponent.Model
import com.sedsoftware.tackle.auth.AuthComponentGateways
import com.sedsoftware.tackle.auth.domain.AuthFlowManager
import com.sedsoftware.tackle.auth.store.AuthStore
import com.sedsoftware.tackle.auth.store.AuthStore.Label
import com.sedsoftware.tackle.auth.store.AuthStoreProvider
import com.sedsoftware.tackle.domain.ComponentOutput
import com.sedsoftware.tackle.domain.api.TackleDispatchers
import com.sedsoftware.tackle.utils.asValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class AuthComponentDefault(
    private val componentContext: ComponentContext,
    private val storeFactory: StoreFactory,
    private val api: AuthComponentGateways.Api,
    private val settings: AuthComponentGateways.Settings,
    private val tools: AuthComponentGateways.Tools,
    private val dispatchers: TackleDispatchers,
    private val output: (ComponentOutput) -> Unit,
) : AuthComponent, ComponentContext by componentContext {

    private val store: AuthStore =
        instanceKeeper.getStore {
            AuthStoreProvider(
                storeFactory = storeFactory,
                manager = AuthFlowManager(api, settings, tools),
                mainContext = dispatchers.main,
                ioContext = dispatchers.io,
            ).create()
        }

    init {
        val scope = CoroutineScope(dispatchers.main)

        scope.launch {
            store.labels.collect { label ->
                when (label) {
                    is Label.NavigateToMainScreen -> output(ComponentOutput.Auth.NavigateToMainScreen)
                    is Label.ErrorCaught -> output(ComponentOutput.Common.ErrorCaught(label.exception))
                }
            }
        }

        lifecycle.doOnDestroy {
            scope.cancel()
        }
    }

    override val model: Value<Model> = store.asValue().map(stateToModel)

    override fun onTextInput(text: String) {
        store.accept(AuthStore.Intent.OnTextInput(text))
    }

    override fun onRetryButtonClick() {
        store.accept(AuthStore.Intent.OnRetryButtonClick)
    }

    override fun onAuthenticateClick() {
        store.accept(AuthStore.Intent.OnAuthenticateButtonClick)
    }

    override fun onShowLearnMore() {
        store.accept(AuthStore.Intent.ShowLearnMore(true))
    }

    override fun onHideLearnMore() {
        store.accept(AuthStore.Intent.ShowLearnMore(false))
    }

    override fun onJoinMastodonClick() {
        store.accept(AuthStore.Intent.ShowLearnMore(false))
        tools.openUrl(JOIN_MASTODON_URL)
    }

    private companion object {
        const val JOIN_MASTODON_URL = "https://joinmastodon.org/"
    }
}
