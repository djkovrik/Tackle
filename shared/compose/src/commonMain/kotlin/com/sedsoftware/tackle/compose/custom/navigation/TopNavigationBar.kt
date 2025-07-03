package com.sedsoftware.tackle.compose.custom.navigation

import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.sedsoftware.tackle.compose.model.TopNavigationTab
import com.sedsoftware.tackle.compose.theme.TackleScreenPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tackle.shared.compose.generated.resources.Res
import tackle.shared.compose.generated.resources.publications_tab_global
import tackle.shared.compose.generated.resources.publications_tab_local

@Composable
internal fun TopNavigationBar(
    selectedTabIndex: Int,
    tabs: List<TopNavigationTab>,
    modifier: Modifier = Modifier,
    tabShape: Shape = MaterialTheme.shapes.medium,
    containerColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = MaterialTheme.colorScheme.onBackground,
    indicatorColor: Color = MaterialTheme.colorScheme.primaryContainer,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium,
) {

    TabRow(
        selectedTabIndex = selectedTabIndex,
        modifier = modifier.clip(shape = tabShape),
        containerColor = containerColor,
        contentColor = contentColor,
        indicator = { tabPositions: List<TabPosition> ->
            if (selectedTabIndex < tabPositions.size) {
                TopNavigationBarIndicator(
                    indicatorShape = tabShape,
                    indicatorColor = indicatorColor,
                    modifier = Modifier.topNavigationBarIndicatorOffset(tabPositions[selectedTabIndex])
                )
            }
        },
        divider = {},
    ) {
        tabs.forEachIndexed { tabIndex: Int, tab: TopNavigationTab ->
            Tab(
                text = {
                    Text(
                        text = stringResource(resource = tab.textResource),
                        style = textStyle,
                    )
                },
                selected = tabIndex == selectedTabIndex,
                interactionSource = DisabledInteractionSource(),
                onClick = { tab.onClick.invoke() },
                modifier = Modifier
                    .clip(shape = tabShape)
                    .zIndex(zIndex = 2f),
            )
        }
    }
}

private class DisabledInteractionSource : MutableInteractionSource {
    override val interactions: Flow<Interaction> = emptyFlow()
    override suspend fun emit(interaction: Interaction) = Unit
    override fun tryEmit(interaction: Interaction) = true
}

@Preview
@Composable
private fun TopNavigationBarPreviewLight() {
    TackleScreenPreview {
        Column {
            TopNavigationBarPreviewContent(0)
            Spacer(modifier = Modifier.height(height = 8.dp))
            TopNavigationBarPreviewContent(1)
        }
    }
}

@Composable
private fun TopNavigationBarPreviewContent(index: Int) {
    TackleScreenPreview {
        TopNavigationBar(
            selectedTabIndex = index,
            tabs = listOf(
                TopNavigationTab(
                    textResource = Res.string.publications_tab_local,
                    onClick = {},
                ),
                TopNavigationTab(
                    textResource = Res.string.publications_tab_global,
                    onClick = {},
                )
            )
        )
    }
}
