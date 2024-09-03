package com.sedsoftware.tackle.compose.ui.editor.child.attachment.content

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.dp

@Suppress("MagicNumber")
internal fun getFraction(index: Int, totalCount: Int): Float = when (index) {
    0 -> if (totalCount > 1) 0.5f else 1f
    1 -> 0.5f
    2 -> if (totalCount > 3) 0.5f else 1f
    else -> 0.5f
}

@Suppress("MagicNumber")
internal fun getPadding(index: Int, totalCount: Int): PaddingValues {
    val padding = 8.dp

    return when (index) {
        0 -> PaddingValues(
            start = 0.dp,
            top = 0.dp,
            end = if (totalCount > 1) padding else 0.dp,
            bottom = if (totalCount > 2) padding else 0.dp,
        )

        1 -> PaddingValues(
            start = padding,
            top = 0.dp,
            end = 0.dp,
            bottom = if (totalCount > 2) padding else 0.dp,
        )

        2 -> PaddingValues(
            start = 0.dp,
            top = padding,
            end = if (totalCount > 3) padding else 0.dp,
            bottom = 0.dp,
        )

        else -> PaddingValues(
            start = padding,
            top = padding,
            end = 0.dp,
            bottom = 0.dp,
        )
    }
}
