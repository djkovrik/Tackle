package com.sedsoftware.tackle.compose.ui.editor.child.attachment.content

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.dp

@Suppress("MagicNumber")
internal fun getFraction(index: Int, totalCount: Int): Float = when {
    index % 2 == 0 -> if (totalCount > index + 1) 0.5f else 1f
    else -> 0.5f
}

@Suppress("MagicNumber")
internal fun getPadding(index: Int, totalCount: Int): PaddingValues {
    val hasLastFullWidth = totalCount % 2 != 0
    val isLast = index == totalCount - 1
    val imageAtTheLeft = index % 2 == 0
    val padding = 4.dp

    return PaddingValues(
        start = if (imageAtTheLeft) 0.dp else padding,
        top = if (index > 1) padding else 0.dp,
        end = when {
            imageAtTheLeft && !isLast && hasLastFullWidth -> padding
            imageAtTheLeft && !hasLastFullWidth -> padding
            else -> 0.dp
        },
        bottom = when {
            hasLastFullWidth && !isLast -> padding
            !hasLastFullWidth && index < totalCount - 2 -> padding
            else -> 0.dp
        },
    )
}
