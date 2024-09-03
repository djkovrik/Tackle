package com.sedsoftware.tackle.compose.ui.editor.child.header.content

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.sedsoftware.tackle.compose.theme.TackleScreenPreview
import com.sedsoftware.tackle.domain.model.AppLocale
import com.sedsoftware.tackle.editor.header.EditorHeaderComponent
import org.jetbrains.compose.resources.stringResource
import tackle.shared.compose.generated.resources.Res
import tackle.shared.compose.generated.resources.common_cancel

@Composable
internal fun LanguageSelectorDialog(
    model: EditorHeaderComponent.Model,
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit = {},
    onConfirmation: (AppLocale) -> Unit = {},
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        LanguageSelectorDialogContent(
            availableLocales = model.availableLocales,
            selectedLocale = model.selectedLocale,
            recommendedLocale = model.recommendedLocale,
            modifier = modifier,
            onDismissRequest = onDismissRequest,
            onConfirmation = onConfirmation,
        )
    }
}

@Composable
private fun LanguageSelectorDialogContent(
    availableLocales: List<AppLocale>,
    selectedLocale: AppLocale,
    recommendedLocale: AppLocale,
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit = {},
    onConfirmation: (AppLocale) -> Unit = {},
) {
    Card(
        modifier = Modifier
            .wrapContentWidth()
            .height(height = 440.dp)
            .padding(all = 16.dp),
        shape = RoundedCornerShape(16.dp),
    ) {
        Column(
            horizontalAlignment = Alignment.End,
            modifier = modifier.fillMaxSize()
        ) {
            LazyColumn(
                modifier = modifier
                    .fillMaxWidth()
                    .weight(weight = 1f, false)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                itemsIndexed(
                    items = availableLocales,
                    key = { _, item -> item.languageCode }
                ) { index: Int, item: AppLocale ->
                    LanguageSelectorItem(
                        checked = item == selectedLocale,
                        default = item == recommendedLocale,
                        language = item.languageName,
                        code = item.languageCode,
                        onClick = { onConfirmation.invoke(item) },
                        modifier = Modifier,
                    )

                    if (index != availableLocales.lastIndex) {
                        HorizontalDivider(
                            thickness = 1.dp,
                            modifier = Modifier.padding(horizontal = 16.dp),
                            color = MaterialTheme.colorScheme.secondary.copy(
                                alpha = 0.5f,
                            )
                        )
                    }
                }
            }

            Text(
                text = stringResource(resource = Res.string.common_cancel),
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .padding(all = 16.dp)
                    .clickable(onClick = onDismissRequest)
            )
        }
    }
}

@Preview
@Composable
private fun LanguageSelectorDialogContentPreviewLight() {
    TackleScreenPreview {
        LanguageSelectorDialogContent(
            availableLocales = listOf(
                AppLocale("English1", "ab"),
                AppLocale("English2", "cd"),
                AppLocale("English3", "ef"),
                AppLocale("English4", "gh"),
                AppLocale("English5", "ij"),
                AppLocale("English6", "kl"),
            ),
            selectedLocale = AppLocale("English3", "ef"),
            recommendedLocale = AppLocale("English1", "ab"),
            modifier = Modifier.width(width = 372.dp)
        )
    }
}
