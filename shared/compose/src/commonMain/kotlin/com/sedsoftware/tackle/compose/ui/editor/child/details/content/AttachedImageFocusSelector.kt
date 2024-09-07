package com.sedsoftware.tackle.compose.ui.editor.child.details.content

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.sedsoftware.tackle.compose.model.TackleImageParams
import com.sedsoftware.tackle.compose.theme.TackleScreenPreview
import com.sedsoftware.tackle.compose.widget.TackleImage
import com.sedsoftware.tackle.editor.details.model.AttachmentParams

@Composable
internal fun AttachedImageFocusSelector(
    url: String,
    focus: Pair<Float, Float>,
    imageParams: AttachmentParams,
    modifier: Modifier = Modifier,
    onFocusChange: (Float, Float) -> Unit = { _, _ -> },
) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .aspectRatio(ratio = imageParams.ratio)
    ) {
        TackleImage(
            data = url,
            contentDescription = null,
            params = TackleImageParams(
                blurhash = imageParams.blurhash,
                width = imageParams.width,
                height = imageParams.height,
                ratio = imageParams.ratio,
            ),
            modifier = modifier
                .fillMaxSize()
                .clip(shape = RoundedCornerShape(size = 4.dp)),
        )
    }
}

@Preview
@Composable
private fun AttachedImageFocusSelectorPreview() {
    TackleScreenPreview {
        AttachedImageFocusSelector(
            url = "",
            focus = 0f to 0f,
            imageParams = AttachmentParams(
                blurhash = "UJOoqX\$P*|oz}@%gELX9+sIW9vrr?GZhxYVs",
                width = 588,
                height = 392,
                ratio = 588f / 392,
            )
        )
    }
}
