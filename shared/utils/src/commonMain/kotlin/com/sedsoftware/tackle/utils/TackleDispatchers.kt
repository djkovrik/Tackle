package com.sedsoftware.tackle.utils

import kotlinx.coroutines.CoroutineDispatcher

interface TackleDispatchers {
    val main: CoroutineDispatcher
    val io: CoroutineDispatcher
    val unconfined: CoroutineDispatcher
}
