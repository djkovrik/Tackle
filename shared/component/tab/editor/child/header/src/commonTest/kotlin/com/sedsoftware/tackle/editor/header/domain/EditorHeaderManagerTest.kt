package com.sedsoftware.tackle.editor.header.domain

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEmpty
import assertk.assertions.isTrue
import com.sedsoftware.tackle.domain.model.AppLocale
import com.sedsoftware.tackle.editor.header.stubs.EditorHeaderSettingsStub
import com.sedsoftware.tackle.editor.header.stubs.EditorHeaderToolsStub
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class EditorHeaderManagerTest {

    private val settings: EditorHeaderSettingsStub = EditorHeaderSettingsStub()
    private val tools: EditorHeaderToolsStub = EditorHeaderToolsStub()
    private val manager: EditorHeaderManager = EditorHeaderManager(settings, tools)

    @Test
    fun `getEditorProfileData returns profile data`() = runTest {
        // given
        settings.avatar = "avatar"
        settings.nickname = "nickname"
        settings.domain = "domain"
        // when
        val result = manager.getEditorProfileData()
        // then
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrThrow().avatar).isEqualTo("avatar")
        assertThat(result.getOrThrow().name).isEqualTo("nickname")
        assertThat(result.getOrThrow().domain).isEqualTo("domain")
    }

    @Test
    fun `getRecommendedLocale returns locale on empty settings`() = runTest {
        // given
        settings.lastSelectedName = ""
        settings.lastSelectedCode = ""
        // when
        val result = manager.getRecommendedLocale()
        // then
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrThrow().languageName).isNotEmpty()
        assertThat(result.getOrThrow().languageCode).isNotEmpty()
    }

    @Test
    fun `getRecommendedLocale returns locale on saved settings`() = runTest {
        // given
        settings.lastSelectedName = "ab"
        settings.lastSelectedCode = "cd"
        // when
        val result = manager.getRecommendedLocale()
        // then
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrThrow().languageName).isEqualTo("ab")
        assertThat(result.getOrThrow().languageCode).isEqualTo("cd")
    }

    @Test
    fun `getAvailableLocales returns non empty list`() = runTest {
        // given
        // when
        val result = manager.getAvailableLocales()
        // then
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrThrow()).isNotEmpty()
    }

    @Test
    fun `saveSelectedLocale saves locale`() = runTest {
        // given
        settings.lastSelectedName = ""
        settings.lastSelectedCode = ""
        val targetLocale = AppLocale(languageName = "ab", languageCode = "cd")
        // when
        val result = manager.saveSelectedLocale(targetLocale)
        // then
        assertThat(result.isSuccess).isTrue()
        assertThat(settings.lastSelectedLanguageName).isEqualTo(targetLocale.languageName)
        assertThat(settings.lastSelectedLanguageCode).isEqualTo(targetLocale.languageCode)
    }
}
