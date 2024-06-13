package com.sedsoftware.tackle.utils.test

import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import com.arkivanov.mvikotlin.core.utils.isAssertOnMainThreadEnabled
import com.sedsoftware.tackle.utils.TackleDispatchers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

abstract class ComponentTest<Component : Any> {

    protected val lifecycle = LifecycleRegistry()

    protected lateinit var component: Component

    protected val testDispatchers: TackleDispatchers =
        object : TackleDispatchers {
            override val main: CoroutineDispatcher = Dispatchers.Unconfined
            override val io: CoroutineDispatcher = Dispatchers.Unconfined
            override val unconfined: CoroutineDispatcher = Dispatchers.Unconfined
        }

    @BeforeTest
    fun beforeTest() {
        isAssertOnMainThreadEnabled = false
        lifecycle.resume()
    }

    @AfterTest
    fun afterTest() {
        isAssertOnMainThreadEnabled = true
    }

    abstract fun createComponent(): Component
}
