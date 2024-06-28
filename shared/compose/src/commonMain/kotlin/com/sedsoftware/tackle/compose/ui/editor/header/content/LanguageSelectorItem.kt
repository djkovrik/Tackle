package com.sedsoftware.tackle.compose.ui.editor.header.content

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.sedsoftware.tackle.compose.theme.TackleScreenPreview
import org.jetbrains.compose.resources.stringResource
import tackle.shared.compose.generated.resources.Res
import tackle.shared.compose.generated.resources.editor_language_default

@Composable
internal fun LanguageSelectorItem(
    checked: Boolean,
    default: Boolean,
    language: String,
    code: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .size(width = 340.dp, height = 54.dp)
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = { checked: Boolean ->
                if (checked) onClick.invoke()
            },
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colorScheme.primary,
                uncheckedColor = MaterialTheme.colorScheme.primary,
                checkmarkColor = MaterialTheme.colorScheme.onPrimary,
            ),
            modifier = Modifier,
        )

        Column(
            modifier = modifier
                .weight(weight = 1f, true)
                .clickable(onClick = onClick)
        ) {
            Text(
                text = language,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            if (default) {
                Text(
                    text = stringResource(Res.string.editor_language_default),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }

        Text(
            text = code,
            color = MaterialTheme.colorScheme.tertiary,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(all = 16.dp)
        )
    }
}

@Composable
@Preview
private fun LanguageSelectorItemNormalPreview() {
    TackleScreenPreview {
        LanguageSelectorItem(
            checked = false,
            default = false,
            language = "English",
            code = "en",
        )
    }
}

@Composable
@Preview
private fun LanguageSelectorItemSelectedPreview() {
    TackleScreenPreview {
        LanguageSelectorItem(
            checked = true,
            default = false,
            language = "English",
            code = "en",
        )
    }
}

@Composable
@Preview
private fun LanguageSelectorItemNormalDefaultPreview() {
    TackleScreenPreview {
        LanguageSelectorItem(
            checked = false,
            default = true,
            language = "English",
            code = "en",
        )
    }
}

@Composable
@Preview
private fun LanguageSelectorItemSelectedDefaultPreview() {
    TackleScreenPreview {
        LanguageSelectorItem(
            checked = true,
            default = true,
            language = "English",
            code = "en",
        )
    }
}
