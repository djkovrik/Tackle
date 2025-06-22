package com.sedsoftware.tackle.status.integration

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.sedsoftware.tackle.domain.model.Status
import com.sedsoftware.tackle.domain.model.Translation
import com.sedsoftware.tackle.status.StatusComponent
import com.sedsoftware.tackle.status.StatusComponent.Model
import com.sedsoftware.tackle.status.model.StatusContextAction

class StatusComponentPreview(
    val status: Status,
    val rebloggedBy: String = "",
    val extendedInfo: Boolean = false,
    val isOwn: Boolean = false,
    val menuVisible: Boolean = false,
    val menuActions: List<StatusContextAction> = emptyList(),
    val translation: Translation? = null,
    val translationInProgress: Boolean = false,
    val translationDisplayed: Boolean = false,
) : StatusComponent {

    override val model: Value<Model> =
        MutableValue(
            Model(
                status = status,
                rebloggedBy = rebloggedBy,
                extendedInfo = extendedInfo,
                isOwn = isOwn,
                menuVisible = menuVisible,
                menuActions = menuActions,
                translation = translation,
                translationInProgress = translationInProgress,
                translationDisplayed = translationDisplayed,
            )
        )

    override fun onMenuActionClick(action: StatusContextAction) = Unit
    override fun onFavouriteClick() = Unit
    override fun onReblogClick() = Unit
    override fun onMenuRequest(visible: Boolean) = Unit
    override fun onPollSelect(index: Int, multiselect: Boolean) = Unit
    override fun onVoteClick() = Unit
    override fun onShareClick() = Unit
    override fun onUrlClick(url: String) = Unit
    override fun onReplyClick() = Unit
    override fun onHashTagClick(hashTag: String) = Unit
    override fun onMentionClick(mention: String) = Unit
    override fun refreshStatus(status: Status) = Unit
    override fun getId(): String = status.id
    override fun activateComponent(activate: Boolean) = Unit
}
