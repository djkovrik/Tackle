package com.sedsoftware.tackle.main

import com.arkivanov.decompose.value.Value
import com.sedsoftware.tackle.domain.model.MediaAttachment
import com.sedsoftware.tackle.domain.model.Status
import com.sedsoftware.tackle.domain.model.Translation
import com.sedsoftware.tackle.main.model.StatusAction
import com.sedsoftware.tackle.main.model.StatusContextAction

interface StatusComponent {

    val model: Value<Model>

    fun onStatusAction(action: StatusAction)
    fun onMenuActionClick(action: StatusContextAction)
    fun onMenuRequest(visible: Boolean)
    fun onPollSelect(index: Int, multiselect: Boolean)
    fun onVoteClick()
    fun onUrlClick(url: String)
    fun onHashTagClick(hashTag: String)
    fun onMentionClick(mention: String)
    fun onAlternateTextRequest(text: String)
    fun onAttachmentClick(attachment: MediaAttachment)
    fun onSensitiveContentToggle()

    data class Model(
        val status: Status,
        val rebloggedBy: String,
        val reblogAvailable: Boolean,
        val extendedInfo: Boolean,
        val isOwn: Boolean,
        val menuVisible: Boolean,
        val menuActions: List<StatusContextAction>,
        val translation: Translation?,
        val translationInProgress: Boolean,
        val translationDisplayed: Boolean,
        val hideSensitiveContent: Boolean,
    )
}
