package com.sedsoftware.tackle.compose.widget

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sedsoftware.tackle.compose.extension.alsoIf

@Composable
fun TackleCheckbox(
    checked: Boolean,
    voted: Boolean,
    expired: Boolean,
    multiselect: Boolean,
    size: Dp,
    borderWidth: Dp,
    backgroundColorUnchecked: Color,
    backgroundColorChecked: Color,
    borderColorUnchecked: Color,
    borderColorChecked: Color,
    checkmarkColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Box(
        modifier = modifier
            .size(size = size)
            .background(
                color = if (checked) {
                    backgroundColorChecked
                } else {
                    backgroundColorUnchecked
                },
                shape = if (multiselect) {
                    MaterialTheme.shapes.extraSmall
                } else {
                    CircleShape
                },
            )
            .border(
                width = borderWidth,
                color = (if (checked) {
                    borderColorChecked
                } else {
                    borderColorUnchecked
                }).copy(
                    alpha = if (expired) 0.25f else 1f,
                ),
                shape = if (multiselect) {
                    MaterialTheme.shapes.extraSmall
                } else {
                    CircleShape
                },
            )
            .clip(
                shape = if (multiselect) {
                    MaterialTheme.shapes.extraSmall
                } else {
                    CircleShape
                }
            )
            .alsoIf(!voted && !expired, Modifier.clickable(onClick = onClick)),
        contentAlignment = Alignment.Center,
    ) {
        AnimatedVisibility(
            visible = checked,
            enter = scaleIn() + fadeIn(),
            exit = scaleOut() + fadeOut(),
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = checkmarkColor,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}
