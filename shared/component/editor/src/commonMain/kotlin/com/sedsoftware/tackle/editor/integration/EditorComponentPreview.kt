package com.sedsoftware.tackle.editor.integration

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.sedsoftware.tackle.domain.model.AppLocale
import com.sedsoftware.tackle.domain.model.CustomEmoji
import com.sedsoftware.tackle.domain.model.type.StatusVisibility
import com.sedsoftware.tackle.editor.EditorComponent
import com.sedsoftware.tackle.editor.EditorComponent.Model
import com.sedsoftware.tackle.editor.attachments.EditorAttachmentsComponent
import com.sedsoftware.tackle.editor.attachments.integration.EditorAttachmentsComponentPreview
import com.sedsoftware.tackle.editor.attachments.model.AttachedFile
import com.sedsoftware.tackle.editor.emojis.EditorEmojisComponent
import com.sedsoftware.tackle.editor.emojis.integration.EditorEmojisComponentPreview
import com.sedsoftware.tackle.editor.header.EditorHeaderComponent
import com.sedsoftware.tackle.editor.header.integration.EditorHeaderComponentPreview
import com.sedsoftware.tackle.editor.model.EditorInputHintItem
import com.sedsoftware.tackle.editor.poll.EditorPollComponent
import com.sedsoftware.tackle.editor.poll.integration.EditorPollComponentPreview
import com.sedsoftware.tackle.editor.poll.model.PollChoiceOption
import com.sedsoftware.tackle.editor.poll.model.PollDuration
import com.sedsoftware.tackle.editor.warning.EditorWarningComponent
import com.sedsoftware.tackle.editor.warning.integration.EditorWarningComponentPreview

class EditorComponentPreview(
    attachments: List<AttachedFile> = emptyList(),
    attachmentsButtonAvailable: Boolean = true,
    attachmentsContentVisible: Boolean = false,
    emojis: List<CustomEmoji> = emptyList(),
    emojisButtonAvailable: Boolean = false,
    emojisContentVisible: Boolean = false,
    avatar: String = "",
    nickname: String = "",
    domain: String = "",
    recommendedLocale: AppLocale = AppLocale.empty(),
    selectedLocale: AppLocale = AppLocale.empty(),
    availableLocales: List<AppLocale> = emptyList(),
    localePickerAvailable: Boolean = false,
    localePickerDisplayed: Boolean = false,
    statusVisibility: StatusVisibility = StatusVisibility.PUBLIC,
    statusVisibilityPickerDisplayed: Boolean = false,
    pollOptions: List<PollChoiceOption> = emptyList(),
    multiselectEnabled: Boolean = false,
    duration: PollDuration = PollDuration.FIVE_MINUTES,
    availableDurations: List<PollDuration> = emptyList(),
    durationPickerVisible: Boolean = false,
    insertionAvailable: Boolean = false,
    deletionAvailable: Boolean = false,
    pollButtonAvailable: Boolean = true,
    pollContentVisible: Boolean = false,
    warningText: String = "",
    warningContentVisible: Boolean = false,
    statusText: String = "",
    statusCharactersLeft: Int = -1,
    suggestions: List<EditorInputHintItem> = emptyList(),
    scheduledDateLabel: String = "",
) : EditorComponent {

    override val attachments: EditorAttachmentsComponent =
        EditorAttachmentsComponentPreview(
            attachments = attachments,
            attachmentsButtonAvailable = attachmentsButtonAvailable,
            attachmentsContentVisible = attachmentsContentVisible,
        )

    override val emojis: EditorEmojisComponent =
        EditorEmojisComponentPreview(
            emojis = emojis,
            emojisButtonAvailable = emojisButtonAvailable,
            emojisContentVisible = emojisContentVisible,
        )

    override val header: EditorHeaderComponent =
        EditorHeaderComponentPreview(
            avatar = avatar,
            nickname = nickname,
            domain = domain,
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
            options = pollOptions,
            multiselectEnabled = multiselectEnabled,
            duration = duration,
            availableDurations = availableDurations,
            durationPickerVisible = durationPickerVisible,
            insertionAvailable = insertionAvailable,
            deletionAvailable = deletionAvailable,
            pollButtonAvailable = pollButtonAvailable,
            pollContentVisible = pollContentVisible,
        )

    override val warning: EditorWarningComponent =
        EditorWarningComponentPreview(
            warningText = warningText,
            warningContentVisible = warningContentVisible,
        )

    override val model: Value<Model> =
        MutableValue(
            Model(
                statusText = statusText,
                statusTextSelection = (statusText.length to statusText.length),
                statusCharactersLeft = statusCharactersLeft,
                suggestions = suggestions,
                datePickerVisible = false,
                scheduledDate = 0L,
                timePickerVisible = false,
                scheduledHour = 0,
                scheduledMinute = 0,
                scheduledIn24hrFormat = false,
                scheduledDateLabel = scheduledDateLabel,
            )
        )

    override fun onTextInput(text: String, selection: Pair<Int, Int>) = Unit
    override fun onEmojiSelected(emoji: CustomEmoji) = Unit
    override fun onInputHintSelected(hint: EditorInputHintItem) = Unit
    override fun onPollButtonClicked() = Unit
    override fun onEmojisButtonClicked() = Unit
    override fun onWarningButtonClicked() = Unit
    override fun onScheduleDatePickerRequested(show: Boolean) = Unit
    override fun onScheduleDateSelected(millis: Long) = Unit
    override fun onScheduleTimePickerRequested(show: Boolean) = Unit
    override fun onScheduleTimeSelected(hour: Int, minute: Int, formatIn24hr: Boolean) = Unit
    override fun resetScheduledDateTime() = Unit
    override fun onSendButtonClicked() = Unit
}
