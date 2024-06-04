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
import com.sedsoftware.tackle.auth.domain.AuthFlowApi
import com.sedsoftware.tackle.auth.domain.AuthFlowManager
import com.sedsoftware.tackle.auth.store.AuthStore
import com.sedsoftware.tackle.auth.store.AuthStore.Label
import com.sedsoftware.tackle.auth.store.AuthStoreProvider
import com.sedsoftware.tackle.network.api.AuthorizedApi
import com.sedsoftware.tackle.network.api.UnauthorizedApi
import com.sedsoftware.tackle.network.response.ApplicationDetails
import com.sedsoftware.tackle.network.response.InstanceDetails
import com.sedsoftware.tackle.network.response.TokenDetails
import com.sedsoftware.tackle.settings.api.TackleSettings
import com.sedsoftware.tackle.utils.TackleDispatchers
import com.sedsoftware.tackle.utils.TacklePlatformTools
import com.sedsoftware.tackle.utils.asValue
import com.sedsoftware.tackle.utils.model.AppClientData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class AuthComponentDefault(
    private val componentContext: ComponentContext,
    private val storeFactory: StoreFactory,
    private val unauthorizedApi: UnauthorizedApi,
    private val authorizedApi: AuthorizedApi,
    private val settings: TackleSettings,
    private val platformTools: TacklePlatformTools,
    private val dispatchers: TackleDispatchers,
    private val output: (AuthComponent.Output) -> Unit,
) : AuthComponent, ComponentContext by componentContext {

    private val store: AuthStore =
        instanceKeeper.getStore {
            AuthStoreProvider(
                storeFactory = storeFactory,
                manager = AuthFlowManager(
                    api = object : AuthFlowApi {
                        override suspend fun getServerInfo(url: String): InstanceDetails =
                            unauthorizedApi.getServerInfo(url)

                        override suspend fun verifyCredentials(): ApplicationDetails =
                            authorizedApi.verifyCredentials()

                        override suspend fun createApp(data: AppClientData): ApplicationDetails =
                            unauthorizedApi.createApp(data.name, data.uri, data.scopes, data.website)

                        override suspend fun obtainToken(id: String, secret: String, code: String, data: AppClientData): TokenDetails =
                            unauthorizedApi.obtainToken(id, secret, code, data.uri, data.scopes)
                    },
                    settings = settings,
                    platformTools = platformTools,
                ),
                mainContext = dispatchers.main,
                ioContext = dispatchers.io,
            ).create()
        }

    init {
        val scope = CoroutineScope(dispatchers.main)

        scope.launch {
            store.labels.collect { label ->
                when (label) {
                    is Label.NavigateToHomeScreen -> output(AuthComponent.Output.NavigateToHomeScreen)
                    is Label.ErrorCaught -> output(AuthComponent.Output.ErrorCaught(label.throwable))
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
        platformTools.openUrl(JOIN_MASTODON_URL)
    }

    private companion object {
        const val JOIN_MASTODON_URL = "https://joinmastodon.org/"
    }
}
