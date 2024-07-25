package com.sedsoftware.tackle.compose.ui.editor.attachment.content

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.dp


internal fun getFraction(index: Int, totalCount: Int): Float = when (index) {
    0 -> if (totalCount > 1) 0.5f else 1f
    1 -> 0.5f
    2 -> if (totalCount > 3) 0.5f else 1f
    else -> 0.5f
}

internal fun getPadding(index: Int, totalCount: Int): PaddingValues {
    val fullPadding = 8.dp
    val halfPadding = fullPadding / 2

    return when (index) {
        0 -> PaddingValues(
            start = fullPadding,
            top = fullPadding,
            end = if (totalCount > 1) halfPadding else fullPadding,
            bottom = if (totalCount > 2) halfPadding else fullPadding,
        )

        1 -> PaddingValues(
            start = halfPadding,
            top = fullPadding,
            end = fullPadding,
            bottom = if (totalCount > 2) halfPadding else fullPadding,
        )

        2 -> PaddingValues(
            start = fullPadding,
            top = halfPadding,
            end = if (totalCount > 3) halfPadding else fullPadding,
            bottom = fullPadding,
        )

        else -> PaddingValues(
            start = halfPadding,
            top = halfPadding,
            end = fullPadding,
            bottom = fullPadding,
        )
    }
}
