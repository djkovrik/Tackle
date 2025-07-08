package com.sedsoftware.tackle.main.integration

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.sedsoftware.tackle.domain.model.Status
import com.sedsoftware.tackle.domain.model.Translation
import com.sedsoftware.tackle.domain.model.type.StatusVisibility.PUBLIC
import com.sedsoftware.tackle.domain.model.type.StatusVisibility.UNLISTED
import com.sedsoftware.tackle.main.StatusComponent
import com.sedsoftware.tackle.main.StatusComponent.Model
import com.sedsoftware.tackle.main.model.StatusAction
import com.sedsoftware.tackle.main.model.StatusContextAction

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
                reblogAvailable = status.visibility == PUBLIC || status.visibility == UNLISTED,
                extendedInfo = extendedInfo,
                isOwn = isOwn,
                menuVisible = menuVisible,
                menuActions = menuActions,
                translation = translation,
                translationInProgress = translationInProgress,
                translationDisplayed = translationDisplayed,
            )
        )

    override fun onStatusAction(action: StatusAction) = Unit
    override fun onMenuActionClick(action: StatusContextAction) = Unit
    override fun onMenuRequest(visible: Boolean) = Unit
    override fun onPollSelect(index: Int, multiselect: Boolean) = Unit
    override fun onVoteClick() = Unit
    override fun onUrlClick(url: String) = Unit
    override fun onHashTagClick(hashTag: String) = Unit
    override fun onMentionClick(mention: String) = Unit
    override fun onAlternateTextRequest(text: String) = Unit
}
