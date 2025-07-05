package com.sedsoftware.tackle.compose.ui.status.content

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.sedsoftware.tackle.compose.extension.clickableOnce
import com.sedsoftware.tackle.compose.model.TackleImageParams
import com.sedsoftware.tackle.compose.widget.TackleImage
import com.sedsoftware.tackle.domain.model.PreviewCard

@Composable
internal fun StatusPreviewCard(
    card: PreviewCard,
    onUrlClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
        ),
        modifier = modifier
            .padding(top = 16.dp)
            .clickableOnce { onUrlClick.invoke(card.url) },
    ) {
        Column(modifier = Modifier.padding(all = 16.dp)) {
            Text(
                text = card.title,
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(bottom = 8.dp),
            )

            Text(
                text = card.providerName,
                color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.75f),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 8.dp),
            )

            if (card.image.isNotEmpty()) {
                TackleImage(
                    imageUrl = card.image,
                    contentDescription = null,
                    params = TackleImageParams(
                        blurhash = card.blurhash,
                        width = card.width,
                        height = card.height,
                        ratio = if (card.height != 0) {
                            card.width / card.height.toFloat()
                        } else {
                            1f
                        },
                    ),
                    modifier = Modifier.clip(shape = MaterialTheme.shapes.small),
                )
            }

            if (card.description.isNotEmpty()) {
                Text(
                    text = card.description,
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 6,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }
}
