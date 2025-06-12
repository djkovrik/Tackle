package com.sedsoftware.tackle.compose.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.OutlinedTextFieldDefaults.Container
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.sedsoftware.tackle.compose.theme.TackleScreenPreview
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun TackleTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    shape: Shape = OutlinedTextFieldDefaults.shape,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(
        unfocusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
        focusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
    ),
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge.copy(
        color = MaterialTheme.colorScheme.onBackground,
    ),
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    val interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }

    BasicTextField(
        value = value,
        modifier = modifier,
        onValueChange = onValueChange,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        interactionSource = interactionSource,
        singleLine = singleLine,
        maxLines = maxLines,
        minLines = minLines,
        decorationBox = @Composable { innerTextField ->
            OutlinedTextFieldDefaults.DecorationBox(
                value = value,
                visualTransformation = visualTransformation,
                innerTextField = innerTextField,
                singleLine = singleLine,
                enabled = enabled,
                isError = isError,
                interactionSource = interactionSource,
                colors = colors,
                label = label,
                placeholder = placeholder,
                trailingIcon = trailingIcon,
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                container = {
                    Container(
                        enabled = enabled,
                        isError = isError,
                        interactionSource = interactionSource,
                        colors = colors,
                        shape = shape,
                        focusedBorderThickness = 1.dp,
                        unfocusedBorderThickness = 1.dp,
                    )
                }
            )
        }
    )
}

@Composable
internal fun TackleTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    shape: Shape = OutlinedTextFieldDefaults.shape,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(
        unfocusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
        focusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
    ),
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge.copy(
        color = MaterialTheme.colorScheme.onBackground,
    ),
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
) {
    val interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }

    BasicTextField(
        value = value,
        modifier = modifier,
        onValueChange = onValueChange,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        interactionSource = interactionSource,
        singleLine = singleLine,
        maxLines = maxLines,
        minLines = minLines,
        decorationBox = @Composable { innerTextField ->
            OutlinedTextFieldDefaults.DecorationBox(
                value = value.text,
                visualTransformation = visualTransformation,
                innerTextField = innerTextField,
                singleLine = singleLine,
                enabled = enabled,
                isError = isError,
                interactionSource = interactionSource,
                colors = colors,
                label = label,
                placeholder = placeholder,
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                container = {
                    Container(
                        enabled = enabled,
                        isError = isError,
                        interactionSource = interactionSource,
                        colors = colors,
                        shape = shape,
                        focusedBorderThickness = 1.dp,
                        unfocusedBorderThickness = 1.dp,
                    )
                }
            )
        }
    )
}

@Preview
@Composable
private fun TackleTextFieldPreviewLight() {
    TackleScreenPreview {
        TackleTextFieldPreviewContent()
    }
}

@Preview
@Composable
private fun TackleTextFieldPreviewDark() {
    TackleScreenPreview(darkTheme = true) {
        TackleTextFieldPreviewContent()
    }
}

@Composable
private fun TackleTextFieldPreviewContent() {
    Column {
        TackleTextField(
            value = "",
            onValueChange = {},
            maxLines = 1,
            singleLine = true,
            modifier = Modifier.padding(all = 4.dp)
        )

        TackleTextField(
            value = "Single line text",
            onValueChange = {},
            maxLines = 1,
            singleLine = true,
            modifier = Modifier.padding(all = 4.dp)
        )

        TackleTextField(
            value = "Multiline\ntext",
            onValueChange = {},
            maxLines = 4,
            singleLine = false,
            modifier = Modifier.padding(all = 4.dp)
        )

        TackleTextField(
            value = "",
            onValueChange = {},
            maxLines = 1,
            singleLine = true,
            modifier = Modifier.padding(all = 4.dp),
            label = {
                Text(
                    text = "Label text",
                    color = MaterialTheme.colorScheme.outline,
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .background(color = MaterialTheme.colorScheme.background)
                )
            },
        )

        TackleTextField(
            value = "Single line text and more text",
            onValueChange = {},
            maxLines = 1,
            singleLine = true,
            modifier = Modifier.padding(all = 4.dp),
            label = {
                Text(
                    text = "Label text",
                    color = MaterialTheme.colorScheme.outline,
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .background(color = MaterialTheme.colorScheme.background)
                )
            },
        )

        TackleTextField(
            value = "",
            onValueChange = {},
            maxLines = 1,
            singleLine = true,
            modifier = Modifier.padding(all = 4.dp),
            placeholder = {
                Text(
                    text = "Placeholder text",
                    color = MaterialTheme.colorScheme.outline,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                )
            },
        )
    }
}
