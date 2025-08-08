package com.sedsoftware.tackle.main.viewmedia.domain

import com.sedsoftware.tackle.main.viewmedia.ViewMediaComponentGateways
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import io.github.vinceglb.filekit.PlatformFile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class FileDownloadManagerTest {

    private val api: ViewMediaComponentGateways.Api = mock {
        everySuspend { downloadFile(any(), any()) } returns MutableStateFlow(0f)
    }
    private val manager: FileDownloadManager = FileDownloadManager(api)

    @Test
    fun `downloadFile should call for api`() = runTest {
        // given
        val url = "url"
        // when
        manager.downloadFile(url, PlatformFile("destination")).first()
        // then
        verifySuspend { api.downloadFile(url, any()) }
    }

}
