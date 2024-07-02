package com.sedsoftware.tackle.compose.ui.editor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.sedsoftware.tackle.compose.ui.editor.header.EditorHeaderContent
import com.sedsoftware.tackle.compose.ui.editor.header.content.LanguageSelectorDialog
import com.sedsoftware.tackle.compose.ui.editor.header.content.VisibilitySelectorDialog
import com.sedsoftware.tackle.editor.EditorTabComponent
import com.sedsoftware.tackle.editor.header.EditorHeaderComponent

@Composable
internal fun EditorTabContent(
    component: EditorTabComponent,
    modifier: Modifier = Modifier,
) {
    val headerComponentModel: EditorHeaderComponent.Model by component.header.model.subscribeAsState()

    Column {

        EditorHeaderContent(
            model = headerComponentModel,
            modifier = modifier,
            onLocalePickerRequest = { component.header.onLocalePickerRequested(true) },
            onVisibilityPickerRequest = { component.header.onStatusVisibilityPickerRequested(true) },
        )

        // Content
        Column(
            modifier = modifier
                .weight(weight = 1f, fill = true)
                .background(color = MaterialTheme.colorScheme.tertiaryContainer)
        ) {

        }

        // Dialogs
        if (headerComponentModel.localePickerDisplayed) {
            LanguageSelectorDialog(
                model = headerComponentModel,
                onDismissRequest = {
                    component.header.onLocalePickerRequested(false)
                },
                onConfirmation = { locale ->
                    component.header.onLocaleSelected(locale)
                    component.header.onLocalePickerRequested(false)
                },
                modifier = modifier,
            )
        }

        if (headerComponentModel.statusVisibilityPickerDisplayed) {
            VisibilitySelectorDialog(
                onDismissRequest = {
                    component.header.onStatusVisibilityPickerRequested(false)
                },
                onConfirmation = { visibility ->
                    component.header.onStatusVisibilitySelected(visibility)
                    component.header.onStatusVisibilityPickerRequested(false)
                },
                modifier = modifier,
            )
        }
    }
}
