package com.sedsoftware.tackle.compose.ui.editor

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.style.TextOverflow.Companion
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.sedsoftware.tackle.compose.theme.TackleScreenPreview
import com.sedsoftware.tackle.compose.ui.editor.content.LanguageSelectorDialog
import com.sedsoftware.tackle.domain.model.AppLocale
import com.sedsoftware.tackle.editor.EditorTabComponent
import com.sedsoftware.tackle.editor.integration.EditorTabComponentPreview
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import tackle.shared.compose.generated.resources.Res
import tackle.shared.compose.generated.resources.editor_language
import tackle.shared.compose.generated.resources.editor_send
import tackle.shared.compose.generated.resources.main_tab_editor

@Composable
internal fun EditorTabContent(
    component: EditorTabComponent,
    modifier: Modifier = Modifier,
) {
    val model: EditorTabComponent.Model by component.model.subscribeAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                actions = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = modifier
                            .height(height = 48.dp)
                            .clip(shape = RoundedCornerShape(size = 32.dp))
                            .clickable { component.onLanguagePickerRequest(true) }
                            .background(color = MaterialTheme.colorScheme.primaryContainer)
                            .padding(horizontal = 16.dp)
                            .weight(weight = 1f, fill = false),
                    ) {
                        Icon(
                            painter = painterResource(resource = Res.drawable.editor_language),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.size(size = 24.dp)
                        )

                        Text(
                            text = model.selectedLocale.languageName,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            style = MaterialTheme.typography.bodyLarge,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(width = 16.dp))

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = modifier
                            .size(size = 48.dp)
                            .background(color = MaterialTheme.colorScheme.primaryContainer, shape = CircleShape)
                            .clip(shape = CircleShape)
                            .clickable { },
                    ) {
                        Icon(
                            painter = painterResource(resource = Res.drawable.editor_send),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.size(size = 24.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                ),
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        },
        modifier = modifier,
    ) { paddingValues: PaddingValues ->
        Column(modifier = modifier.padding(paddingValues)) {
            if (model.localePickerVisible) {
                LanguageSelectorDialog(
                    model = model,
                    onDismissRequest = {
                        component.onLanguagePickerRequest(false)
                    },
                    onConfirmation = { locale ->
                        component.onLocaleSelect(locale)
                        component.onLanguagePickerRequest(false)
                    },
                    modifier = modifier,
                )
            }
        }
    }
}

@Preview
@Composable
private fun EditorTabContentIdlePreview() {
    TackleScreenPreview {
        EditorTabContent(
            component = EditorTabComponentPreview(
                selectedLocale = AppLocale("English", "en")
            )
        )
    }
}
