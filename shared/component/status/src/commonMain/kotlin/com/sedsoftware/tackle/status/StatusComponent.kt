package com.sedsoftware.tackle.status

import com.arkivanov.decompose.value.Value
import com.sedsoftware.tackle.domain.model.Status
import com.sedsoftware.tackle.domain.model.Translation
import com.sedsoftware.tackle.status.model.StatusContextAction

interface StatusComponent {

    val model: Value<Model>

    fun onMenuActionClick(action: StatusContextAction)
    fun onFavouriteClick()
    fun onReblogClick()
    fun onMenuRequest(visible: Boolean)
    fun onPollSelect(index: Int, multiselect: Boolean)
    fun onVoteClick()
    fun onShareClick()
    fun onUrlClick(url: String)
    fun onReplyClick()
    fun onHashTagClick(hashTag: String)
    fun onMentionClick(mention: String)
    fun getId(): String
    fun stopComponent()
    fun resumeComponent()

    data class Model(
        val status: Status,
        val rebloggedBy: String,
        val extendedInfo: Boolean,
        val isOwn: Boolean,
        val menuVisible: Boolean,
        val menuActions: List<StatusContextAction>,
        val translation: Translation?,
        val translationInProgress: Boolean,
        val translationDisplayed: Boolean,
    )
}
