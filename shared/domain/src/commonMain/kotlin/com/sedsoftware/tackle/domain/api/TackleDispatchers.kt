package com.sedsoftware.tackle.domain.api

import kotlinx.coroutines.CoroutineDispatcher

interface TackleDispatchers {
    val main: CoroutineDispatcher
    val io: CoroutineDispatcher
    val unconfined: CoroutineDispatcher
}
