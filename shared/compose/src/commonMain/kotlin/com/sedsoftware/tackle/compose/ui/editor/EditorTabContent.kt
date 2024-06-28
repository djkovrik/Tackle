package com.sedsoftware.tackle.compose.ui.editor

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
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

    Scaffold(
        topBar = {
            EditorHeaderContent(
                model = headerComponentModel,
                modifier = modifier,
                onLocalePickerRequest = { component.header.onLocalePickerRequested(true) },
                onVisibilityPickerRequest = { component.header.onStatusVisibilityPickerRequested(true) },
            )
        },
        modifier = modifier,
    ) { paddingValues: PaddingValues ->

        // Content
        Column(modifier = modifier.padding(paddingValues)) {

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
