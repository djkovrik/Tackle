package com.sedsoftware.tackle.compose.ui.auth.part

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.sedsoftware.tackle.compose.theme.TackleScreenPreview
import org.jetbrains.compose.resources.stringResource
import tackle.shared.compose.generated.resources.Res
import tackle.shared.compose.generated.resources.auth_join
import tackle.shared.compose.generated.resources.auth_mastodon_description
import tackle.shared.compose.generated.resources.auth_mastodon_title
import tackle.shared.compose.generated.resources.auth_no_account
import tackle.shared.compose.generated.resources.auth_server_description

@Composable
internal fun LearnMoreBottomSheet(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(all = 16.dp)
    ) {
        Text(
            text = stringResource(Res.string.auth_mastodon_title),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.headlineMedium,
            modifier = modifier.padding(bottom = 8.dp),
        )

        Text(
            text = stringResource(Res.string.auth_mastodon_description),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyLarge,
            modifier = modifier.padding(bottom = 16.dp),
        )

        Text(
            text = stringResource(Res.string.auth_server_description),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyLarge,
            modifier = modifier.padding(bottom = 8.dp),
        )

        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text(
                text = stringResource(Res.string.auth_no_account),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyLarge,
                modifier = modifier
                    .padding(top = 8.dp, bottom = 8.dp, end = 8.dp),
            )

            Text(
                text = stringResource(Res.string.auth_join),
                textDecoration = TextDecoration.Underline,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyLarge,
                modifier = modifier
                    .padding(vertical = 8.dp)
                    .clickable { TODO("Open url") },
            )
        }
    }
}

@Composable
@Preview
private fun PreviewLearnMoreBottomSheetLight() {
    TackleScreenPreview {
        LearnMoreBottomSheet()
    }
}

@Composable
@Preview
private fun PreviewLearnMoreBottomSheetDark() {
    TackleScreenPreview(darkTheme = true) {
        LearnMoreBottomSheet()
    }
}
