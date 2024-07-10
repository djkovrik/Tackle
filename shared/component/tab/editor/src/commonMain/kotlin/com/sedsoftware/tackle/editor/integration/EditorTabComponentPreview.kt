package com.sedsoftware.tackle.editor.integration

import com.sedsoftware.tackle.domain.model.AppLocale
import com.sedsoftware.tackle.domain.model.CustomEmoji
import com.sedsoftware.tackle.domain.model.type.StatusVisibility
import com.sedsoftware.tackle.editor.EditorTabComponent
import com.sedsoftware.tackle.editor.attachments.EditorAttachmentsComponent
import com.sedsoftware.tackle.editor.attachments.integration.EditorAttachmentsComponentPreview
import com.sedsoftware.tackle.editor.attachments.model.AttachedFile
import com.sedsoftware.tackle.editor.emojis.EditorEmojisComponent
import com.sedsoftware.tackle.editor.emojis.integration.EditorEmojisComponentPreview
import com.sedsoftware.tackle.editor.header.EditorHeaderComponent
import com.sedsoftware.tackle.editor.header.integration.EditorHeaderComponentPreview
import com.sedsoftware.tackle.editor.warning.EditorWarningComponent
import com.sedsoftware.tackle.editor.warning.integration.EditorWarningComponentPreview

class EditorTabComponentPreview(
    attachments: List<AttachedFile> = emptyList(),
    attachmentsAvailable: Boolean = true,
    avatar: String = "",
    nickname: String = "",
    recommendedLocale: AppLocale = AppLocale.empty(),
    selectedLocale: AppLocale = AppLocale.empty(),
    availableLocales: List<AppLocale> = emptyList(),
    localePickerAvailable: Boolean = false,
    localePickerDisplayed: Boolean = false,
    statusVisibility: StatusVisibility = StatusVisibility.PUBLIC,
    statusVisibilityPickerDisplayed: Boolean = false,
    emojis: List<CustomEmoji> = emptyList(),
    emojiPickerAvailable: Boolean = false,
    warningText: String = "",
) : EditorTabComponent {

    override val attachments: EditorAttachmentsComponent =
        EditorAttachmentsComponentPreview(
            attachments = attachments,
            available = attachmentsAvailable,
        )


    override val header: EditorHeaderComponent =
        EditorHeaderComponentPreview(
            avatar = avatar,
            nickname = nickname,
            recommendedLocale = recommendedLocale,
            selectedLocale = selectedLocale,
            availableLocales = availableLocales,
            localePickerAvailable = localePickerAvailable,
            localePickerDisplayed = localePickerDisplayed,
            statusVisibility = statusVisibility,
            statusVisibilityPickerDisplayed = statusVisibilityPickerDisplayed
        )

    override val emojis: EditorEmojisComponent =
        EditorEmojisComponentPreview(
            emojis = emojis,
            emojiPickerAvailable = emojiPickerAvailable,
        )

    override val warning: EditorWarningComponent =
        EditorWarningComponentPreview(warningText)
}
