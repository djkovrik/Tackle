package com.sedsoftware.tackle.editor.integration

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.sedsoftware.tackle.domain.model.AppLocale
import com.sedsoftware.tackle.domain.model.CustomEmoji
import com.sedsoftware.tackle.domain.model.type.StatusVisibility
import com.sedsoftware.tackle.editor.EditorTabComponent
import com.sedsoftware.tackle.editor.EditorTabComponent.Model
import com.sedsoftware.tackle.editor.attachments.EditorAttachmentsComponent
import com.sedsoftware.tackle.editor.attachments.integration.EditorAttachmentsComponentPreview
import com.sedsoftware.tackle.editor.attachments.model.AttachedFile
import com.sedsoftware.tackle.editor.emojis.EditorEmojisComponent
import com.sedsoftware.tackle.editor.emojis.integration.EditorEmojisComponentPreview
import com.sedsoftware.tackle.editor.header.EditorHeaderComponent
import com.sedsoftware.tackle.editor.header.integration.EditorHeaderComponentPreview
import com.sedsoftware.tackle.editor.poll.EditorPollComponent
import com.sedsoftware.tackle.editor.poll.integration.EditorPollComponentPreview
import com.sedsoftware.tackle.editor.poll.model.PollDuration
import com.sedsoftware.tackle.editor.poll.model.PollOption
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
    options: List<PollOption> = emptyList(),
    multiselectEnabled: Boolean = false,
    duration: PollDuration = PollDuration.FIVE_MINUTES,
    availableDurations: List<PollDuration> = emptyList(),
    durationPickerVisible: Boolean = false,
    insertionAvailable: Boolean = false,
    deletionAvailable: Boolean = false,
    pollButtonAvailable: Boolean = false,
    maxOptionTextLength: Int = 1,
    warningText: String = "",
    statusText: String = "",
    statusCharactersLeft: Int = -1,
    attachmentsActive: Boolean = false,
    emojisActive: Boolean = false,
    pollActive: Boolean = false,
    warningActive: Boolean = false,
    sendingAvailable: Boolean = false,
) : EditorTabComponent {

    override val attachments: EditorAttachmentsComponent =
        EditorAttachmentsComponentPreview(
            attachments = attachments,
            available = attachmentsAvailable,
        )

    override val emojis: EditorEmojisComponent =
        EditorEmojisComponentPreview(
            emojis = emojis,
            emojiPickerAvailable = emojiPickerAvailable,
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

    override val poll: EditorPollComponent =
        EditorPollComponentPreview(
            options = options,
            multiselectEnabled = multiselectEnabled,
            duration = duration,
            availableDurations = availableDurations,
            durationPickerVisible = durationPickerVisible,
            insertionAvailable = insertionAvailable,
            deletionAvailable = deletionAvailable,
            pollButtonAvailable = pollButtonAvailable,
            maxOptionTextLength = maxOptionTextLength,
        )

    override val warning: EditorWarningComponent =
        EditorWarningComponentPreview(warningText)

    override val model: Value<Model> =
        MutableValue(
            Model(
                statusText = statusText,
                statusCharactersLeft = statusCharactersLeft,
                attachmentsActive = attachmentsActive,
                emojisActive = emojisActive,
                pollActive = pollActive,
                warningActive = warningActive,
                sendingAvailable = sendingAvailable,
            )
        )

    override fun onTextInput(text: String) = Unit
    override fun onEmojiSelected(emoji: CustomEmoji) = Unit
    override fun onAttachmentsButtonClicked() = Unit
    override fun onPollButtonClicked() = Unit
    override fun onEmojisButtonClicked() = Unit
    override fun onWarningButtonClicked() = Unit
    override fun onSendButtonClicked() = Unit
}
