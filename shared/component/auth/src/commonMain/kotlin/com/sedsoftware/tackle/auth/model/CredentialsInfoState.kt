package com.sedsoftware.tackle.auth.model

enum class CredentialsInfoState {
    NOT_CHECKED, EXISTING_USER_CHECK_FAILED, RETRYING, UNAUTHORIZED, AUTHORIZED;
}
