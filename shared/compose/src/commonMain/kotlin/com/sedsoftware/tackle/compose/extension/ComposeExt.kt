package com.sedsoftware.tackle.compose.extension

import androidx.compose.foundation.clickable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import kotlinx.coroutines.delay

internal fun Modifier.alsoIf(condition: Boolean, other: Modifier) = if (condition) this.then(other) else this

internal fun Modifier.clickableOnce(
    onClick: () -> Unit,
): Modifier = this.then(
    composed(
        inspectorInfo = {
            name = "clickableOnce"
            value = onClick
        },
    ) {
        var enableAgain: Boolean by remember { mutableStateOf(true) }
        LaunchedEffect(
            enableAgain,
            block = {
                if (enableAgain) return@LaunchedEffect
                delay(timeMillis = CLICKS_DEBOUNCE_MS)
                enableAgain = true
            },
        )
        Modifier.clickable {
            if (enableAgain) {
                enableAgain = false
                onClick()
            }
        }
    },
)

private const val CLICKS_DEBOUNCE_MS = 500L
