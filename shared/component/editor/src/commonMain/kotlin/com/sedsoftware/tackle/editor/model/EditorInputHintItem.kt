package com.sedsoftware.tackle.editor.model

import com.sedsoftware.tackle.domain.model.Account
import com.sedsoftware.tackle.domain.model.CustomEmoji
import com.sedsoftware.tackle.domain.model.HashTag

sealed class EditorInputHintItem {
    data class Account(val avatar: String, val username: String, val acct: String) : EditorInputHintItem()
    data class Emoji(val shortcode: String, val url: String) : EditorInputHintItem()
    data class HashTag(val text: String) : EditorInputHintItem()
}

internal fun Account.toEditorInputHintAccount(): EditorInputHintItem =
    EditorInputHintItem.Account(
        avatar = this.avatarStatic,
        username = this.username,
        acct = this.acct,
    )

internal fun CustomEmoji.toEditorInputHintEmoji(): EditorInputHintItem =
    EditorInputHintItem.Emoji(
        shortcode = this.shortcode,
        url = this.staticUrl,
    )

internal fun HashTag.toEditorInputHintHashTag(): EditorInputHintItem =
    EditorInputHintItem.HashTag(
        text = this.name,
    )
