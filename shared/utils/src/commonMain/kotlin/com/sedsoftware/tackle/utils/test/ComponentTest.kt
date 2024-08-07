package com.sedsoftware.tackle.utils.test

import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import com.arkivanov.mvikotlin.core.utils.isAssertOnMainThreadEnabled
import com.sedsoftware.tackle.domain.api.TackleDispatchers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

abstract class ComponentTest<Component : Any> {

    protected val lifecycle: LifecycleRegistry = LifecycleRegistry()

    protected val testDispatchers: TackleDispatchers =
        object : TackleDispatchers {
            override val main: CoroutineDispatcher = Dispatchers.Unconfined
            override val io: CoroutineDispatcher = Dispatchers.Unconfined
            override val unconfined: CoroutineDispatcher = Dispatchers.Unconfined
        }

    protected val component: Component
        get() = _component!!

    private var _component: Component? = null

    // TODO Bring annotations back when this fixed
    // https://youtrack.jetbrains.com/issue/KT-62368
    fun beforeTest() {
        _component = createComponent()
        isAssertOnMainThreadEnabled = false
        lifecycle.resume()
    }

    fun afterTest() {
        isAssertOnMainThreadEnabled = true
        _component = null
    }

    abstract fun createComponent(): Component
}
