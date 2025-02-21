package com.sedsoftware.tackle.utils.test

import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import com.arkivanov.mvikotlin.core.utils.isAssertOnMainThreadEnabled
import com.sedsoftware.tackle.domain.ComponentOutput
import com.sedsoftware.tackle.domain.api.TackleDispatchers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

abstract class ComponentTest<Component : Any> {

    protected val lifecycle: LifecycleRegistry = LifecycleRegistry()

    protected val componentOutput: MutableList<ComponentOutput> = mutableListOf()

    protected val testDispatchers: TackleDispatchers =
        object : TackleDispatchers {
            override val main: CoroutineDispatcher = Dispatchers.Unconfined
            override val io: CoroutineDispatcher = Dispatchers.Unconfined
            override val unconfined: CoroutineDispatcher = Dispatchers.Unconfined
        }

    protected val component: Component
        get() = _component!!

    private var _component: Component? = null

    fun setUp() {
        _component = createComponent()
        isAssertOnMainThreadEnabled = false
        lifecycle.resume()
    }

    fun tearDown() {
        isAssertOnMainThreadEnabled = true
        _component = null
        componentOutput.clear()
    }

    abstract fun createComponent(): Component
}
