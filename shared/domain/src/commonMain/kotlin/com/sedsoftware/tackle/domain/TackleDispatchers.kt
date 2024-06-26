package com.sedsoftware.tackle.domain

import kotlinx.coroutines.CoroutineDispatcher

interface TackleDispatchers {
    val main: CoroutineDispatcher
    val io: CoroutineDispatcher
    val unconfined: CoroutineDispatcher
}
