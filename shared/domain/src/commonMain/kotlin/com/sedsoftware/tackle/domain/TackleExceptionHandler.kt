package com.sedsoftware.tackle.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class TackleExceptionHandler(private val logoutAction: () -> Unit = {}) {

    val messaging: Flow<TackleException>
        get() = _messaging

    private val _messaging: MutableStateFlow<TackleException> = MutableStateFlow(TackleException.Empty)

    private val exceptionActions: Map<TackleException.Action, (TackleException) -> Unit> = mapOf(
        TackleException.Action.SHOW_MESSAGE to { _messaging.value = it },
        TackleException.Action.LOGOUT to { logoutAction.invoke() },
    )

    fun consume(throwable: Throwable) {
        val target = throwable.wrap()
        exceptionActions[target.action]?.invoke(target)
    }

    private fun Throwable.wrap(): TackleException =
        if (this !is TackleException) {
            TackleException.Unknown(this)
        } else {
            this
        }
}
