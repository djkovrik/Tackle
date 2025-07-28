package com.sedsoftware.tackle.compose.ui.editor.child.details

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.sedsoftware.tackle.compose.TackleScreenDefaults
import com.sedsoftware.tackle.compose.TackleScreenTemplate
import com.sedsoftware.tackle.compose.theme.TackleScreenPreview
import com.sedsoftware.tackle.compose.ui.editor.child.details.content.AttachedImageFocusSelector
import com.sedsoftware.tackle.compose.widget.TackleAppBarButton
import com.sedsoftware.tackle.compose.widget.TackleTextField
import com.sedsoftware.tackle.domain.model.type.MediaAttachmentType
import com.sedsoftware.tackle.editor.details.EditorAttachmentDetailsComponent
import com.sedsoftware.tackle.editor.details.integration.EditorAttachmentDetailsComponentPreview
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tackle.shared.compose.generated.resources.Res
import tackle.shared.compose.generated.resources.editor_attachment_alternate_text
import tackle.shared.compose.generated.resources.editor_attachment_alternate_text_description
import tackle.shared.compose.generated.resources.editor_attachment_focus
import tackle.shared.compose.generated.resources.editor_back
import tackle.shared.compose.generated.resources.editor_content_description_send
import tackle.shared.compose.generated.resources.editor_done

@Composable
internal fun EditorAttachmentDetailsContent(
    component: EditorAttachmentDetailsComponent,
    modifier: Modifier = Modifier,
) {
    val model: EditorAttachmentDetailsComponent.Model by component.model.subscribeAsState()

    TackleScreenTemplate(
        title = null,
        navigationIcon = Res.drawable.editor_back,
        onNavigationClick = component::onBackButtonClick,
        colors = TackleScreenDefaults.colors(
            headerContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            headerContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        ),
        actions = {
            AnimatedVisibility(
                visible = model.loading,
                enter = scaleIn() + fadeIn(),
                exit = scaleOut() + fadeOut(),
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(size = 32.dp),
                )
            }

            Spacer(modifier = Modifier.width(width = 8.dp))

            TackleAppBarButton(
                iconRes = Res.drawable.editor_done,
                contentDescriptionRes = Res.string.editor_content_description_send,
                onClick = component::onUpdateButtonClick,
                enabled = model.dataChanged,
            )
        },
        modifier = modifier,
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                Text(
                    text = stringResource(resource = Res.string.editor_attachment_alternate_text_description),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 32.dp)
                )
            }

            item {
                TackleTextField(
                    value = model.description,
                    onValueChange = component::onAttachmentDescriptionInput,
                    maxLines = 6,
                    textStyle = MaterialTheme.typography.bodyMedium,
                    placeholder = {
                        Text(
                            text = stringResource(resource = Res.string.editor_attachment_alternate_text),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(
                                alpha = 0.5f,
                            ),
                        )
                    },
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .fillMaxWidth()
                )
            }

            item {
                if (model.type == MediaAttachmentType.IMAGE || model.type == MediaAttachmentType.VIDEO) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = stringResource(resource = Res.string.editor_attachment_focus),
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, end = 16.dp, top = 16.dp)
                        )

                        AttachedImageFocusSelector(
                            url = model.url,
                            focus = model.focus,
                            imageParams = model.params,
                            onFocusChange = component::onAttachmentFocusInput,
                            modifier = Modifier.padding(all = 16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun EditorAttachmentDetailsContentPreviewLight() {
    TackleScreenPreview {
        EditorAttachmentDetailsPreviewContent()
    }
}

@Preview
@Composable
private fun EditorAttachmentDetailsContentPreviewSending() {
    TackleScreenPreview {
        EditorAttachmentDetailsPreviewContent(
            description = "Context description",
            sending = true,
        )
    }
}

@Preview
@Composable
private fun EditorAttachmentDetailsContentPreviewDark() {
    TackleScreenPreview(darkTheme = true) {
        EditorAttachmentDetailsPreviewContent()
    }
}

@Composable
private fun EditorAttachmentDetailsPreviewContent(
    description: String = "",
    updatingAvailable: Boolean = true,
    sending: Boolean = false,
) {
    EditorAttachmentDetailsContent(
        EditorAttachmentDetailsComponentPreview(
            description = description,
            updatingAvailable = updatingAvailable,
            sending = sending,
        )
    )
}
