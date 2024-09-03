package com.sedsoftware.tackle.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class TackleExceptionHandler(private val logoutAction: () -> Unit = {}) {

    val messaging: Flow<TackleException>
        get() = _messaging

    private val _messaging: MutableSharedFlow<TackleException> = MutableSharedFlow()

    private val exceptionActions: Map<TackleException.Action, suspend (TackleException) -> Unit> = mapOf(
        TackleException.Action.SHOW_MESSAGE to { _messaging.emit(it) },
        TackleException.Action.LOGOUT to { logoutAction.invoke() },
    )

    fun consume(throwable: Throwable, scope: CoroutineScope) {
        val target: TackleException = throwable.wrap()
        scope.launch { exceptionActions[target.action]?.invoke(target) }
    }

    private fun Throwable.wrap(): TackleException =
        if (this !is TackleException) {
            TackleException.Unknown(this)
        } else {
            this
        }
}
