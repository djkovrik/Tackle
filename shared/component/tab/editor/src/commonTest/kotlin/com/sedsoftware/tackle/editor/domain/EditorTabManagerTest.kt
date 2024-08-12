package com.sedsoftware.tackle.editor.domain

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isNotEmpty
import assertk.assertions.isTrue
import com.sedsoftware.tackle.editor.EditorTabComponentGateways
import com.sedsoftware.tackle.editor.model.EditorInputHintRequest
import com.sedsoftware.tackle.editor.stubs.EditorTabComponentApiStub
import com.sedsoftware.tackle.editor.stubs.EditorTabComponentDatabaseStub
import com.sedsoftware.tackle.editor.stubs.EditorTabComponentToolsStub
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class EditorTabManagerTest {

    private val api: EditorTabComponentApiStub = EditorTabComponentApiStub()
    private val database: EditorTabComponentGateways.Database = EditorTabComponentDatabaseStub()
    private val tools: EditorTabComponentGateways.Tools = EditorTabComponentToolsStub()
    private val manager: EditorTabManager = EditorTabManager(api, database, tools)

    @Test
    fun `getCachedInstanceInfo should return instance info`() = runTest {
        // given
        // when
        val result = manager.getCachedInstanceInfo()
        // then
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrThrow().domain).isNotEmpty()
    }

    @Test
    fun `searchForAccounts should return accounts successfully`() = runTest {
        // given
        val query = "query"
        // when
        val result = manager.searchForAccounts(query)
        // then
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrThrow()).isNotEmpty()
    }

    @Test
    fun `searchForAccounts should return failure on api error`() = runTest {
        // given
        val query = "query"
        api.shouldThrowException = true
        // when
        val result = manager.searchForAccounts(query)
        // then
        assertThat(result.isSuccess).isFalse()
        assertThat(result.isFailure).isTrue()
    }

    @Test
    fun `searchForEmojis should return accounts successfully`() = runTest {
        // given
        val query = "query"
        // when
        val result = manager.searchForEmojis(query)
        // then
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrThrow()).isNotEmpty()
    }

    @Test
    fun `searchForHashTags should return accounts successfully`() = runTest {
        // given
        val query = "query"
        // when
        val result = manager.searchForHashTags(query)
        // then
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrThrow()).isNotEmpty()
    }

    @Test
    fun `searchForHashTags should return failure on api error`() = runTest {
        // given
        val query = "query"
        api.shouldThrowException = true
        // when
        val result = manager.searchForHashTags(query)
        // then
        assertThat(result.isSuccess).isFalse()
        assertThat(result.isFailure).isTrue()
    }

    @Test
    fun `checkForInputHint should skip sequences with no marker`() = runTest {
        // given
        val text = "This is some text here"
        val results = mutableListOf<EditorInputHintRequest>()
        // when
        for (index in text.indices) {
            results.add(manager.checkForInputHint(text, index to index))
        }
        // then
        assertThat(results.count { it !is EditorInputHintRequest.None }).isEqualTo(0)
    }

    @Test
    fun `checkForInputHint should skip too short sequences`() {
        // given
        val text1 = "This is some text: more :t and : two"
        val text2 = "#a"
        val text3 = "@"
        val results = mutableListOf<EditorInputHintRequest>()
        // when
        results.add(manager.checkForInputHint(text1, 13 to 13))
        results.add(manager.checkForInputHint(text1, 14 to 14))
        results.add(manager.checkForInputHint(text1, 15 to 15))
        results.add(manager.checkForInputHint(text1, 16 to 16))
        results.add(manager.checkForInputHint(text1, 17 to 17))
        results.add(manager.checkForInputHint(text1, 18 to 18))
        results.add(manager.checkForInputHint(text1, 24 to 24))
        results.add(manager.checkForInputHint(text1, 25 to 25))
        results.add(manager.checkForInputHint(text1, 26 to 26))
        results.add(manager.checkForInputHint(text1, 31 to 31))
        results.add(manager.checkForInputHint(text1, 32 to 32))
        results.add(manager.checkForInputHint(text2, 0 to 0))
        results.add(manager.checkForInputHint(text2, 1 to 1))
        results.add(manager.checkForInputHint(text2, 2 to 2))
        results.add(manager.checkForInputHint(text3, 0 to 0))
        results.add(manager.checkForInputHint(text3, 1 to 1))
        // then
        assertThat(results.count { it !is EditorInputHintRequest.None }).isEqualTo(0)
    }

    @Test
    fun `checkForInputHint should detect account input suggest`() = runTest {
        // given
        val mentionOnly = "@ab"
        // when
        val resultEmojiOnly0 = manager.checkForInputHint(mentionOnly, 0 to 0)
        val resultEmojiOnly1 = manager.checkForInputHint(mentionOnly, 1 to 1)
        val resultEmojiOnly2 = manager.checkForInputHint(mentionOnly, 2 to 2)
        val resultEmojiOnly3 = manager.checkForInputHint(mentionOnly, 3 to 3)
        // then
        assertThat(resultEmojiOnly0).isEqualTo(EditorInputHintRequest.None)
        assertThat(resultEmojiOnly1).isEqualTo(EditorInputHintRequest.Accounts("@ab"))
        assertThat(resultEmojiOnly2).isEqualTo(EditorInputHintRequest.Accounts("@ab"))
        assertThat(resultEmojiOnly3).isEqualTo(EditorInputHintRequest.Accounts("@ab"))

        // given
        val mentionOnlyMoreInput = "@abc"
        // when
        val mentionOnlyMoreInput0 = manager.checkForInputHint(mentionOnlyMoreInput, 0 to 0)
        val mentionOnlyMoreInput1 = manager.checkForInputHint(mentionOnlyMoreInput, 1 to 1)
        val mentionOnlyMoreInput2 = manager.checkForInputHint(mentionOnlyMoreInput, 2 to 2)
        val mentionOnlyMoreInput3 = manager.checkForInputHint(mentionOnlyMoreInput, 3 to 3)
        val mentionOnlyMoreInput4 = manager.checkForInputHint(mentionOnlyMoreInput, 4 to 4)
        // then
        assertThat(mentionOnlyMoreInput0).isEqualTo(EditorInputHintRequest.None)
        assertThat(mentionOnlyMoreInput1).isEqualTo(EditorInputHintRequest.Accounts("@abc"))
        assertThat(mentionOnlyMoreInput2).isEqualTo(EditorInputHintRequest.Accounts("@abc"))
        assertThat(mentionOnlyMoreInput3).isEqualTo(EditorInputHintRequest.Accounts("@abc"))
        assertThat(mentionOnlyMoreInput4).isEqualTo(EditorInputHintRequest.Accounts("@abc"))

        // given
        val mentionAndSomeText = "@abc test"
        // when
        val mentionAndSomeText0 = manager.checkForInputHint(mentionAndSomeText, 0 to 0)
        val mentionAndSomeText1 = manager.checkForInputHint(mentionAndSomeText, 1 to 1)
        val mentionAndSomeText2 = manager.checkForInputHint(mentionAndSomeText, 2 to 2)
        val mentionAndSomeText3 = manager.checkForInputHint(mentionAndSomeText, 3 to 3)
        val mentionAndSomeText4 = manager.checkForInputHint(mentionAndSomeText, 4 to 4)
        val mentionAndSomeText5 = manager.checkForInputHint(mentionAndSomeText, 5 to 5)
        // then
        assertThat(mentionAndSomeText0).isEqualTo(EditorInputHintRequest.None)
        assertThat(mentionAndSomeText1).isEqualTo(EditorInputHintRequest.Accounts("@abc"))
        assertThat(mentionAndSomeText2).isEqualTo(EditorInputHintRequest.Accounts("@abc"))
        assertThat(mentionAndSomeText3).isEqualTo(EditorInputHintRequest.Accounts("@abc"))
        assertThat(mentionAndSomeText4).isEqualTo(EditorInputHintRequest.Accounts("@abc"))
        assertThat(mentionAndSomeText5).isEqualTo(EditorInputHintRequest.None)

        // given
        val mentionInTheMiddle = "Some text @abc more text."
        // when
        val mentionInTheMiddle10 = manager.checkForInputHint(mentionInTheMiddle, 10 to 10)
        val mentionInTheMiddle11 = manager.checkForInputHint(mentionInTheMiddle, 11 to 11)
        val mentionInTheMiddle12 = manager.checkForInputHint(mentionInTheMiddle, 12 to 12)
        val mentionInTheMiddle13 = manager.checkForInputHint(mentionInTheMiddle, 13 to 13)
        val mentionInTheMiddle14 = manager.checkForInputHint(mentionInTheMiddle, 14 to 14)
        val mentionInTheMiddle15 = manager.checkForInputHint(mentionInTheMiddle, 15 to 15)
        // then
        assertThat(mentionInTheMiddle10).isEqualTo(EditorInputHintRequest.None)
        assertThat(mentionInTheMiddle11).isEqualTo(EditorInputHintRequest.Accounts("@abc"))
        assertThat(mentionInTheMiddle12).isEqualTo(EditorInputHintRequest.Accounts("@abc"))
        assertThat(mentionInTheMiddle13).isEqualTo(EditorInputHintRequest.Accounts("@abc"))
        assertThat(mentionInTheMiddle14).isEqualTo(EditorInputHintRequest.Accounts("@abc"))
        assertThat(mentionInTheMiddle15).isEqualTo(EditorInputHintRequest.None)

        // given
        val accountAtTheEnd = "Some text and more @abc"
        // when
        val accountAtTheEnd19 = manager.checkForInputHint(accountAtTheEnd, 19 to 19)
        val accountAtTheEnd20 = manager.checkForInputHint(accountAtTheEnd, 20 to 20)
        val accountAtTheEnd21 = manager.checkForInputHint(accountAtTheEnd, 21 to 21)
        val accountAtTheEnd22 = manager.checkForInputHint(accountAtTheEnd, 22 to 22)
        val accountAtTheEnd23 = manager.checkForInputHint(accountAtTheEnd, 23 to 23)
        // then
        assertThat(accountAtTheEnd19).isEqualTo(EditorInputHintRequest.None)
        assertThat(accountAtTheEnd20).isEqualTo(EditorInputHintRequest.Accounts("@abc"))
        assertThat(accountAtTheEnd21).isEqualTo(EditorInputHintRequest.Accounts("@abc"))
        assertThat(accountAtTheEnd22).isEqualTo(EditorInputHintRequest.Accounts("@abc"))
        assertThat(accountAtTheEnd23).isEqualTo(EditorInputHintRequest.Accounts("@abc"))
    }

    @Test
    fun `checkForInputHint should detect emojis input suggest`() = runTest {
        // given
        val emojiOnly = ":ab"
        // when
        val resultEmojiOnly0 = manager.checkForInputHint(emojiOnly, 0 to 0)
        val resultEmojiOnly1 = manager.checkForInputHint(emojiOnly, 1 to 1)
        val resultEmojiOnly2 = manager.checkForInputHint(emojiOnly, 2 to 2)
        val resultEmojiOnly3 = manager.checkForInputHint(emojiOnly, 3 to 3)
        // then
        assertThat(resultEmojiOnly0).isEqualTo(EditorInputHintRequest.None)
        assertThat(resultEmojiOnly1).isEqualTo(EditorInputHintRequest.Emojis(":ab"))
        assertThat(resultEmojiOnly2).isEqualTo(EditorInputHintRequest.Emojis(":ab"))
        assertThat(resultEmojiOnly3).isEqualTo(EditorInputHintRequest.Emojis(":ab"))

        // given
        val emojiOnlyMoreInput = ":abc"
        // when
        val emojiOnlyMoreInput0 = manager.checkForInputHint(emojiOnlyMoreInput, 0 to 0)
        val emojiOnlyMoreInput1 = manager.checkForInputHint(emojiOnlyMoreInput, 1 to 1)
        val emojiOnlyMoreInput2 = manager.checkForInputHint(emojiOnlyMoreInput, 2 to 2)
        val emojiOnlyMoreInput3 = manager.checkForInputHint(emojiOnlyMoreInput, 3 to 3)
        val emojiOnlyMoreInput4 = manager.checkForInputHint(emojiOnlyMoreInput, 4 to 4)
        // then
        assertThat(emojiOnlyMoreInput0).isEqualTo(EditorInputHintRequest.None)
        assertThat(emojiOnlyMoreInput1).isEqualTo(EditorInputHintRequest.Emojis(":abc"))
        assertThat(emojiOnlyMoreInput2).isEqualTo(EditorInputHintRequest.Emojis(":abc"))
        assertThat(emojiOnlyMoreInput3).isEqualTo(EditorInputHintRequest.Emojis(":abc"))
        assertThat(emojiOnlyMoreInput4).isEqualTo(EditorInputHintRequest.Emojis(":abc"))

        // given
        val emojiAndSomeText = ":abc test"
        // when
        val emojiAndSomeText0 = manager.checkForInputHint(emojiAndSomeText, 0 to 0)
        val emojiAndSomeText1 = manager.checkForInputHint(emojiAndSomeText, 1 to 1)
        val emojiAndSomeText2 = manager.checkForInputHint(emojiAndSomeText, 2 to 2)
        val emojiAndSomeText3 = manager.checkForInputHint(emojiAndSomeText, 3 to 3)
        val emojiAndSomeText4 = manager.checkForInputHint(emojiAndSomeText, 4 to 4)
        val emojiAndSomeText5 = manager.checkForInputHint(emojiAndSomeText, 5 to 5)
        // then
        assertThat(emojiAndSomeText0).isEqualTo(EditorInputHintRequest.None)
        assertThat(emojiAndSomeText1).isEqualTo(EditorInputHintRequest.Emojis(":abc"))
        assertThat(emojiAndSomeText2).isEqualTo(EditorInputHintRequest.Emojis(":abc"))
        assertThat(emojiAndSomeText3).isEqualTo(EditorInputHintRequest.Emojis(":abc"))
        assertThat(emojiAndSomeText4).isEqualTo(EditorInputHintRequest.Emojis(":abc"))
        assertThat(emojiAndSomeText5).isEqualTo(EditorInputHintRequest.None)

        // given
        val emojiClosed = ":abc: test"
        // when
        val emojiClosed0 = manager.checkForInputHint(emojiClosed, 0 to 0)
        val emojiClosed1 = manager.checkForInputHint(emojiClosed, 1 to 1)
        val emojiClosed2 = manager.checkForInputHint(emojiClosed, 2 to 2)
        val emojiClosed3 = manager.checkForInputHint(emojiClosed, 3 to 3)
        val emojiClosed4 = manager.checkForInputHint(emojiClosed, 4 to 4)
        val emojiClosed5 = manager.checkForInputHint(emojiClosed, 5 to 5)
        // then
        assertThat(emojiClosed0).isEqualTo(EditorInputHintRequest.None)
        assertThat(emojiClosed1).isEqualTo(EditorInputHintRequest.Emojis(":abc:"))
        assertThat(emojiClosed2).isEqualTo(EditorInputHintRequest.Emojis(":abc:"))
        assertThat(emojiClosed3).isEqualTo(EditorInputHintRequest.Emojis(":abc:"))
        assertThat(emojiClosed4).isEqualTo(EditorInputHintRequest.Emojis(":abc:"))
        assertThat(emojiClosed5).isEqualTo(EditorInputHintRequest.None)

        // given
        val emojiInTheMiddle = "Some text :abc more text."
        // when
        val emojiInTheMiddle10 = manager.checkForInputHint(emojiInTheMiddle, 10 to 10)
        val emojiInTheMiddle11 = manager.checkForInputHint(emojiInTheMiddle, 11 to 11)
        val emojiInTheMiddle12 = manager.checkForInputHint(emojiInTheMiddle, 12 to 12)
        val emojiInTheMiddle13 = manager.checkForInputHint(emojiInTheMiddle, 13 to 13)
        val emojiInTheMiddle14 = manager.checkForInputHint(emojiInTheMiddle, 14 to 14)
        val emojiInTheMiddle15 = manager.checkForInputHint(emojiInTheMiddle, 15 to 15)
        // then
        assertThat(emojiInTheMiddle10).isEqualTo(EditorInputHintRequest.None)
        assertThat(emojiInTheMiddle11).isEqualTo(EditorInputHintRequest.Emojis(":abc"))
        assertThat(emojiInTheMiddle12).isEqualTo(EditorInputHintRequest.Emojis(":abc"))
        assertThat(emojiInTheMiddle13).isEqualTo(EditorInputHintRequest.Emojis(":abc"))
        assertThat(emojiInTheMiddle14).isEqualTo(EditorInputHintRequest.Emojis(":abc"))
        assertThat(emojiInTheMiddle15).isEqualTo(EditorInputHintRequest.None)

        // given
        val emojiAtTheEnd = "Some text and more :abc"
        // when
        val emojiAtTheEnd19 = manager.checkForInputHint(emojiAtTheEnd, 19 to 19)
        val emojiAtTheEnd20 = manager.checkForInputHint(emojiAtTheEnd, 20 to 20)
        val emojiAtTheEnd21 = manager.checkForInputHint(emojiAtTheEnd, 21 to 21)
        val emojiAtTheEnd22 = manager.checkForInputHint(emojiAtTheEnd, 22 to 22)
        val emojiAtTheEnd23 = manager.checkForInputHint(emojiAtTheEnd, 23 to 23)
        // then
        assertThat(emojiAtTheEnd19).isEqualTo(EditorInputHintRequest.None)
        assertThat(emojiAtTheEnd20).isEqualTo(EditorInputHintRequest.Emojis(":abc"))
        assertThat(emojiAtTheEnd21).isEqualTo(EditorInputHintRequest.Emojis(":abc"))
        assertThat(emojiAtTheEnd22).isEqualTo(EditorInputHintRequest.Emojis(":abc"))
        assertThat(emojiAtTheEnd23).isEqualTo(EditorInputHintRequest.Emojis(":abc"))
    }


    @Test
    fun `checkForInputHint should detect hashtag input suggest`() = runTest {
        // given
        val hashtagOnly = "#ab"
        // when
        val resultEmojiOnly0 = manager.checkForInputHint(hashtagOnly, 0 to 0)
        val resultEmojiOnly1 = manager.checkForInputHint(hashtagOnly, 1 to 1)
        val resultEmojiOnly2 = manager.checkForInputHint(hashtagOnly, 2 to 2)
        val resultEmojiOnly3 = manager.checkForInputHint(hashtagOnly, 3 to 3)
        // then
        assertThat(resultEmojiOnly0).isEqualTo(EditorInputHintRequest.None)
        assertThat(resultEmojiOnly1).isEqualTo(EditorInputHintRequest.HashTags("#ab"))
        assertThat(resultEmojiOnly2).isEqualTo(EditorInputHintRequest.HashTags("#ab"))
        assertThat(resultEmojiOnly3).isEqualTo(EditorInputHintRequest.HashTags("#ab"))

        // given
        val hashtagOnlyMoreInput = "#abc"
        // when
        val hashtagOnlyMoreInput0 = manager.checkForInputHint(hashtagOnlyMoreInput, 0 to 0)
        val hashtagOnlyMoreInput1 = manager.checkForInputHint(hashtagOnlyMoreInput, 1 to 1)
        val hashtagOnlyMoreInput2 = manager.checkForInputHint(hashtagOnlyMoreInput, 2 to 2)
        val hashtagOnlyMoreInput3 = manager.checkForInputHint(hashtagOnlyMoreInput, 3 to 3)
        val hashtagOnlyMoreInput4 = manager.checkForInputHint(hashtagOnlyMoreInput, 4 to 4)
        // then
        assertThat(hashtagOnlyMoreInput0).isEqualTo(EditorInputHintRequest.None)
        assertThat(hashtagOnlyMoreInput1).isEqualTo(EditorInputHintRequest.HashTags("#abc"))
        assertThat(hashtagOnlyMoreInput2).isEqualTo(EditorInputHintRequest.HashTags("#abc"))
        assertThat(hashtagOnlyMoreInput3).isEqualTo(EditorInputHintRequest.HashTags("#abc"))
        assertThat(hashtagOnlyMoreInput4).isEqualTo(EditorInputHintRequest.HashTags("#abc"))

        // given
        val hashtagAndSomeText = "#abc test"
        // when
        val hashtagAndSomeText0 = manager.checkForInputHint(hashtagAndSomeText, 0 to 0)
        val hashtagAndSomeText1 = manager.checkForInputHint(hashtagAndSomeText, 1 to 1)
        val hashtagAndSomeText2 = manager.checkForInputHint(hashtagAndSomeText, 2 to 2)
        val hashtagAndSomeText3 = manager.checkForInputHint(hashtagAndSomeText, 3 to 3)
        val hashtagAndSomeText4 = manager.checkForInputHint(hashtagAndSomeText, 4 to 4)
        val hashtagAndSomeText5 = manager.checkForInputHint(hashtagAndSomeText, 5 to 5)
        // then
        assertThat(hashtagAndSomeText0).isEqualTo(EditorInputHintRequest.None)
        assertThat(hashtagAndSomeText1).isEqualTo(EditorInputHintRequest.HashTags("#abc"))
        assertThat(hashtagAndSomeText2).isEqualTo(EditorInputHintRequest.HashTags("#abc"))
        assertThat(hashtagAndSomeText3).isEqualTo(EditorInputHintRequest.HashTags("#abc"))
        assertThat(hashtagAndSomeText4).isEqualTo(EditorInputHintRequest.HashTags("#abc"))
        assertThat(hashtagAndSomeText5).isEqualTo(EditorInputHintRequest.None)

        // given
        val hashtagInTheMiddle = "Some text #abc more text."
        // when
        val hashtagInTheMiddle10 = manager.checkForInputHint(hashtagInTheMiddle, 10 to 10)
        val hashtagInTheMiddle11 = manager.checkForInputHint(hashtagInTheMiddle, 11 to 11)
        val hashtagInTheMiddle12 = manager.checkForInputHint(hashtagInTheMiddle, 12 to 12)
        val hashtagInTheMiddle13 = manager.checkForInputHint(hashtagInTheMiddle, 13 to 13)
        val hashtagInTheMiddle14 = manager.checkForInputHint(hashtagInTheMiddle, 14 to 14)
        val hashtagInTheMiddle15 = manager.checkForInputHint(hashtagInTheMiddle, 15 to 15)
        // then
        assertThat(hashtagInTheMiddle10).isEqualTo(EditorInputHintRequest.None)
        assertThat(hashtagInTheMiddle11).isEqualTo(EditorInputHintRequest.HashTags("#abc"))
        assertThat(hashtagInTheMiddle12).isEqualTo(EditorInputHintRequest.HashTags("#abc"))
        assertThat(hashtagInTheMiddle13).isEqualTo(EditorInputHintRequest.HashTags("#abc"))
        assertThat(hashtagInTheMiddle14).isEqualTo(EditorInputHintRequest.HashTags("#abc"))
        assertThat(hashtagInTheMiddle15).isEqualTo(EditorInputHintRequest.None)

        // given
        val hashtagAtTheEnd = "Some text and more #abc"
        // when
        val hashtagAtTheEnd19 = manager.checkForInputHint(hashtagAtTheEnd, 19 to 19)
        val hashtagAtTheEnd20 = manager.checkForInputHint(hashtagAtTheEnd, 20 to 20)
        val hashtagAtTheEnd21 = manager.checkForInputHint(hashtagAtTheEnd, 21 to 21)
        val hashtagAtTheEnd22 = manager.checkForInputHint(hashtagAtTheEnd, 22 to 22)
        val hashtagAtTheEnd23 = manager.checkForInputHint(hashtagAtTheEnd, 23 to 23)
        // then
        assertThat(hashtagAtTheEnd19).isEqualTo(EditorInputHintRequest.None)
        assertThat(hashtagAtTheEnd20).isEqualTo(EditorInputHintRequest.HashTags("#abc"))
        assertThat(hashtagAtTheEnd21).isEqualTo(EditorInputHintRequest.HashTags("#abc"))
        assertThat(hashtagAtTheEnd22).isEqualTo(EditorInputHintRequest.HashTags("#abc"))
        assertThat(hashtagAtTheEnd23).isEqualTo(EditorInputHintRequest.HashTags("#abc"))
    }
}
