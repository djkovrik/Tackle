package com.sedsoftware.tackle.domain

sealed class ComponentOutput {

    sealed class Auth {
        data object NavigateToMainScreen : ComponentOutput()
    }

    sealed class Common {
        data class ErrorCaught(val throwable: Throwable) : ComponentOutput()
    }
}
