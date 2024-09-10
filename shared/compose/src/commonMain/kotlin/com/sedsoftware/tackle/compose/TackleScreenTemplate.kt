package com.sedsoftware.tackle.compose

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sedsoftware.tackle.compose.theme.TackleScreenPreview
import com.sedsoftware.tackle.compose.widget.TackleIconButton
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import tackle.shared.compose.generated.resources.Res
import tackle.shared.compose.generated.resources.editor_back
import tackle.shared.compose.generated.resources.editor_done
import tackle.shared.compose.generated.resources.editor_title

@Composable
internal fun TackleScreenTemplate(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 8.dp,
    title: StringResource? = null,
    navigationIcon: DrawableResource? = null,
    onNavigationClick: () -> Unit = {},
    colors: TackleScreenColors = TackleScreenDefaults.colors(),
    actions: @Composable RowScope.() -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    content: @Composable () -> Unit = {},
) {
    val hasColoredHeader: Boolean = remember { colors.headerContainerColor != colors.bodyContainerColor }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (title != null) {
                        Text(
                            text = stringResource(resource = title),
                            color = colors.headerContentColor,
                            style = MaterialTheme.typography.titleSmall,
                        )
                    }
                },
                navigationIcon = {
                    if (navigationIcon != null) {
                        TackleIconButton(
                            iconRes = navigationIcon,
                            containerColor = colors.headerContainerColor,
                            contentColor = colors.headerContentColor,
                            onClick = onNavigationClick,
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colors.headerContainerColor,
                    navigationIconContentColor = colors.headerContentColor,
                    titleContentColor = colors.headerContentColor,
                    actionIconContentColor = colors.headerContentColor,
                ),
                actions = actions,
                modifier = modifier.padding(horizontal = 16.dp),
            )
        },
        floatingActionButton = floatingActionButton,
        containerColor = if (hasColoredHeader) colors.headerContainerColor else colors.bodyContainerColor,
        contentColor = colors.bodyContentColor,
        modifier = modifier,
    ) { paddingValues: PaddingValues ->
        Surface(
            shape = if (hasColoredHeader) RoundedCornerShape(topStart = cornerRadius, topEnd = cornerRadius) else RectangleShape,
            color = colors.bodyContainerColor,
            modifier = modifier.padding(paddingValues = paddingValues)
        ) {
            content()
        }
    }
}

@Stable
internal class TackleScreenColors(
    val headerContainerColor: Color,
    val headerContentColor: Color,
    val bodyContainerColor: Color,
    val bodyContentColor: Color,
)

internal object TackleScreenDefaults {

    @Composable
    fun colors(
        headerContainerColor: Color = MaterialTheme.colorScheme.surface,
        headerContentColor: Color = MaterialTheme.colorScheme.onSurface,
        bodyContainerColor: Color = MaterialTheme.colorScheme.surface,
        bodyContentColor: Color = MaterialTheme.colorScheme.onSurface,
    ) = TackleScreenColors(headerContainerColor, headerContentColor, bodyContainerColor, bodyContentColor)
}

@Preview
@Composable
private fun TackleScreenTemplatePreview() {
    TackleScreenPreview {
        TackleScreenTemplatePreviewContent(
            navigationIcon = Res.drawable.editor_back,
        )
    }
}

@Preview
@Composable
private fun TackleScreenTemplatePreviewDark() {
    TackleScreenPreview(darkTheme = true) {
        TackleScreenTemplatePreviewContent(
            navigationIcon = Res.drawable.editor_back,
        )
    }
}

@Preview
@Composable
private fun TackleScreenTemplatePreviewNoBackIcon() {
    TackleScreenPreview {
        TackleScreenTemplatePreviewContent(
            navigationIcon = null,
        )
    }
}

@Preview
@Composable
private fun TackleScreenTemplatePreviewColored() {
    TackleScreenPreview {
        TackleScreenTemplatePreviewContent(
            cornerRadius = 32.dp,
            navigationIcon = Res.drawable.editor_back,
            colors = TackleScreenDefaults.colors(
                headerContainerColor = MaterialTheme.colorScheme.primaryContainer,
                headerContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                bodyContainerColor = MaterialTheme.colorScheme.inverseSurface,
                bodyContentColor = MaterialTheme.colorScheme.inverseOnSurface
            )
        )
    }
}

@Composable
private fun TackleScreenTemplatePreviewContent(
    cornerRadius: Dp = 8.dp,
    navigationIcon: DrawableResource? = null,
    colors: TackleScreenColors = TackleScreenDefaults.colors(),
) {
    TackleScreenTemplate(
        cornerRadius = cornerRadius,
        title = Res.string.editor_title,
        navigationIcon = navigationIcon,
        colors = colors,
        actions = {
            IconButton(onClick = {}) {
                Icon(
                    painter = painterResource(resource = Res.drawable.editor_done),
                    contentDescription = null,
                    modifier = Modifier.size(size = 24.dp)
                )
            }
        },
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(text = "Screen content")
        }
    }
}
