package com.sedsoftware.tackle.compose.model

import org.jetbrains.compose.resources.StringResource

internal data class TopNavigationTab(
    val textResource: StringResource,
    val onClick: () -> Unit,
)
