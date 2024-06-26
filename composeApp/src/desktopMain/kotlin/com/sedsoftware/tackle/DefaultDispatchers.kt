package com.sedsoftware.tackle

import com.sedsoftware.tackle.domain.TackleDispatchers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.swing.Swing

object DefaultDispatchers : TackleDispatchers {
    override val main: CoroutineDispatcher = Dispatchers.Swing
    override val io: CoroutineDispatcher = Dispatchers.IO
    override val unconfined: CoroutineDispatcher = Dispatchers.Unconfined
}
