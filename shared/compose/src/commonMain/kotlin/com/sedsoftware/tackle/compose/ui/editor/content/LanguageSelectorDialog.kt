package com.sedsoftware.tackle.compose.ui.editor.content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.sedsoftware.tackle.domain.model.AppLocale
import com.sedsoftware.tackle.editor.EditorTabComponent
import org.jetbrains.compose.resources.stringResource
import tackle.shared.compose.generated.resources.Res
import tackle.shared.compose.generated.resources.common_cancel

@Composable
internal fun LanguageSelectorDialog(
    model: EditorTabComponent.Model,
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit = {},
    onConfirmation: (AppLocale) -> Unit = {},
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .wrapContentWidth()
                .height(height = 440.dp)
                .padding(all = 16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = modifier.fillMaxSize()
            ) {
                LazyColumn(
                    modifier = modifier
                        .fillMaxWidth()
                        .weight(weight = 1f, false)
                        .padding(all = 16.dp)
                ) {
                    itemsIndexed(
                        items = model.availableLocales,
                        key = { _, item -> item.languageCode }
                    ) { index: Int, item: AppLocale ->
                        LanguageSelectorItem(
                            checked = item == model.selectedLocale,
                            default = item == model.recommendedLocale,
                            language = item.languageName,
                            code = item.languageCode,
                            onClick = { onConfirmation.invoke(item) },
                            modifier = Modifier,
                        )

                        if (index != model.availableLocales.lastIndex) {
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
                    text = stringResource(Res.string.common_cancel),
                    color = MaterialTheme.colorScheme.tertiary,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .padding(all = 16.dp)
                        .clickable(onClick = onDismissRequest)
                )
            }
        }
    }
}
