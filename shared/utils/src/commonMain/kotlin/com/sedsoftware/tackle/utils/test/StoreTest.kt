package com.sedsoftware.tackle.utils.test

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.utils.isAssertOnMainThreadEnabled
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

abstract class StoreTest<Intent : Any, State : Any, Label : Any> {

    protected val labels: MutableList<Label> = mutableListOf()

    protected lateinit var store: Store<Intent, State, Label>

    private var labelsJob: Job? = null

    @BeforeTest
    fun beforeTest() {
        isAssertOnMainThreadEnabled = false

        store = createStore()

        @OptIn(DelicateCoroutinesApi::class)
        labelsJob = GlobalScope.launch(Dispatchers.Unconfined) {
            store.labels.collect { value -> labels.add(value) }
        }
    }

    @AfterTest
    fun afterTest() {
        isAssertOnMainThreadEnabled = true

        labels.clear()
        labelsJob?.cancel()
        labelsJob = null
    }

    abstract fun createStore(): Store<Intent, State, Label>
}
