package com.sedsoftware.tackle.auth.model

enum class CredentialsState {
    NOT_CHECKED, EXISTING_USER_CHECK_FAILED, RETRYING, UNAUTHORIZED, AUTHORIZED;
}
