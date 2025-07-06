package com.sedsoftware.tackle.utils

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import assertk.assertions.startsWith
import com.sedsoftware.tackle.domain.TackleException
import com.sedsoftware.tackle.domain.model.PlatformFileWrapper
import com.sedsoftware.tackle.utils.extension.focusToOffset
import com.sedsoftware.tackle.utils.extension.isAudio
import com.sedsoftware.tackle.utils.extension.isImage
import com.sedsoftware.tackle.utils.extension.isUnauthorized
import com.sedsoftware.tackle.utils.extension.isValidUrl
import com.sedsoftware.tackle.utils.extension.isVideo
import com.sedsoftware.tackle.utils.extension.offsetToFocus
import com.sedsoftware.tackle.utils.extension.orFalse
import com.sedsoftware.tackle.utils.extension.orZero
import com.sedsoftware.tackle.utils.extension.toDurationFormat
import com.sedsoftware.tackle.utils.extension.toVideoDuration
import com.sedsoftware.tackle.utils.extension.toHumanReadableSize
import com.sedsoftware.tackle.utils.extension.toLocalDateCustom
import com.sedsoftware.tackle.utils.extension.toLocalDateTimeCustom
import com.sedsoftware.tackle.utils.extension.toNormalizedUrl
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.TimeZone
import kotlin.test.Test

class ExtensionsTest {

    private val testDateTime1 = "2024-06-03T12:34:56.000Z"
    private val testDateTime2 = "2024-03-02T00:00:00.000Z"
    private val testDate1 = "2024-06-22"
    private val testDate2 = "1998-01-30"
    private val incorrect = "24/06/22"

    @Test
    fun `toLocalDateTime should parse correct date time string`() = runTest {
        // given
        // when
        val dateTime1 = testDateTime1.toLocalDateTimeCustom(timeZone = TimeZone.UTC)
        val dateTime2 = testDateTime2.toLocalDateTimeCustom(timeZone = TimeZone.UTC)
        // then
        assertThat(dateTime1.year).isEqualTo(2024)
        assertThat(dateTime1.monthNumber).isEqualTo(6)
        assertThat(dateTime1.dayOfMonth).isEqualTo(3)
        assertThat(dateTime1.hour).isEqualTo(12)
        assertThat(dateTime1.minute).isEqualTo(34)
        assertThat(dateTime1.second).isEqualTo(56)

        assertThat(dateTime2.year).isEqualTo(2024)
        assertThat(dateTime2.monthNumber).isEqualTo(3)
        assertThat(dateTime2.dayOfMonth).isEqualTo(2)
        assertThat(dateTime2.hour).isEqualTo(0)
        assertThat(dateTime2.minute).isEqualTo(0)
        assertThat(dateTime2.second).isEqualTo(0)
    }

    @Test
    fun `toLocalDateTime should use fallback for incorrect date time string`() = runTest {
        // given
        // when
        val dateTime = incorrect.toLocalDateTimeCustom(timeZone = TimeZone.UTC)
        // then
        assertThat(dateTime.year).isEqualTo(1970)
        assertThat(dateTime.monthNumber).isEqualTo(1)
        assertThat(dateTime.dayOfMonth).isEqualTo(1)
        assertThat(dateTime.hour).isEqualTo(0)
        assertThat(dateTime.minute).isEqualTo(0)
        assertThat(dateTime.second).isEqualTo(0)
    }

    @Test
    fun `toLocalDate should parse correct date string`() = runTest {
        // given
        // when
        val date1 = testDate1.toLocalDateCustom()
        val date2 = testDate2.toLocalDateCustom()
        // then
        assertThat(date1.year).isEqualTo(2024)
        assertThat(date1.monthNumber).isEqualTo(6)
        assertThat(date1.dayOfMonth).isEqualTo(22)

        assertThat(date2.year).isEqualTo(1998)
        assertThat(date2.monthNumber).isEqualTo(1)
        assertThat(date2.dayOfMonth).isEqualTo(30)
    }

    @Test
    fun `toLocalDate should use fallback date for incorrect date string`() = runTest {
        // given
        // when
        val date = incorrect.toLocalDateCustom()
        // then
        assertThat(date.year).isEqualTo(1970)
        assertThat(date.monthNumber).isEqualTo(1)
        assertThat(date.dayOfMonth).isEqualTo(1)
    }

    @Test
    fun `isUnauthorized should return true for unathorized and false otherwise`() = runTest {
        // given
        val unauthorized = TackleException.RemoteServerException("test", 401, "test")
        val misc = TackleException.Unknown(IllegalStateException("test"))
        // when
        // then
        assertThat(unauthorized.isUnauthorized).isTrue()
        assertThat(misc.isUnauthorized).isFalse()
    }

    @Test
    fun `orZero should return this or zero`() = runTest {
        // given
        val x1: Long? = 123L
        val x2: Long? = null
        val x3: Int? = 123
        val x4: Int? = null
        val x5: Float? = 123.4f
        val x6: Float? = null
        // when
        // then
        assertThat(x1.orZero()).isEqualTo(123L)
        assertThat(x2.orZero()).isEqualTo(0L)
        assertThat(x3.orZero()).isEqualTo(123)
        assertThat(x4.orZero()).isEqualTo(0)
        assertThat(x5.orZero()).isEqualTo(123.4f)
        assertThat(x6.orZero()).isEqualTo(0f)
    }

    @Test
    fun `orFalse should return this or false`() = runTest {
        // given
        val x1: Boolean? = true
        val x2: Boolean? = null
        // when
        // then
        assertThat(x1.orFalse()).isTrue()
        assertThat(x2.orFalse()).isFalse()
    }

    @Test
    fun `mimeType extensions should return correct value`() = runTest {
        // given
        val audio = PlatformFileWrapper(
            name = "test",
            extension = "test",
            path = "",
            mimeType = "audio/ogg",
            size = 0L,
            sizeLabel = "",
            readBytes = { ByteArray(0) })

        val image = PlatformFileWrapper(
            name = "test",
            extension = "test",
            path = "",
            mimeType = "image/jpeg",
            size = 0L,
            sizeLabel = "",
            readBytes = { ByteArray(0) })

        val video = PlatformFileWrapper(
            name = "test",
            extension = "test",
            path = "",
            mimeType = "video/mp4",
            size = 0L,
            sizeLabel = "",
            readBytes = { ByteArray(0) })
        // when
        // then
        assertThat(audio.isAudio).isTrue()
        assertThat(audio.isImage).isFalse()
        assertThat(audio.isVideo).isFalse()

        assertThat(image.isAudio).isFalse()
        assertThat(image.isImage).isTrue()
        assertThat(image.isVideo).isFalse()

        assertThat(video.isAudio).isFalse()
        assertThat(video.isImage).isFalse()
        assertThat(video.isVideo).isTrue()
    }

    @Test
    fun `toHumanReadableSize should format long values as decimal`() = runTest {
        // given
        val bytes = 123L
        val kilobytes = 12345L
        val megabytes = 45678901L
        // when
        val bytesFormatted = bytes.toHumanReadableSize()
        val kilobytesFormatted = kilobytes.toHumanReadableSize()
        val megabytesFormatted = megabytes.toHumanReadableSize()
        // then
        assertThat(bytesFormatted).isEqualTo("123.0 b")
        assertThat(kilobytesFormatted).isEqualTo("12.3 Kb")
        assertThat(megabytesFormatted).isEqualTo("45.7 Mb")
    }

    @Test
    fun `toHumanReadableSize should format long values as binary`() = runTest {
        // given
        val bytes = 123L
        val kilobytes = 12345L
        val megabytes = 45678901L
        // when
        val bytesFormatted = bytes.toHumanReadableSize(useBinary = true)
        val kilobytesFormatted = kilobytes.toHumanReadableSize(useBinary = true)
        val megabytesFormatted = megabytes.toHumanReadableSize(useBinary = true)
        // then
        assertThat(bytesFormatted).isEqualTo("123.0 b")
        assertThat(kilobytesFormatted).isEqualTo("12.1 Kb")
        assertThat(megabytesFormatted).isEqualTo("43.6 Mb")
    }

    @Test
    fun `focusToOffset should calculate correct offset`() = runTest {
        // given
        val middlePointX = 540f
        val middlePointY = 360f
        val focusTopLeft = -1f to 1f
        val focusTopRight = 1f to 1f
        val focusBottomLeft = -1f to -1f
        val focusBottomRight = 1f to -1f
        val expectedTopLeft = -middlePointX to -middlePointY
        val expectedTopRight = middlePointX to -middlePointY
        val expectedBottomLeft = -middlePointX to middlePointY
        val expectedBottomRight = middlePointX to middlePointY
        // when
        val resultTopLeft = focusTopLeft.focusToOffset(middlePointX, middlePointY)
        val resultTopRight = focusTopRight.focusToOffset(middlePointX, middlePointY)
        val resultBottomLeft = focusBottomLeft.focusToOffset(middlePointX, middlePointY)
        val resultBottomRight = focusBottomRight.focusToOffset(middlePointX, middlePointY)
        // then
        assertThat(resultTopLeft).isEqualTo(expectedTopLeft)
        assertThat(resultTopRight).isEqualTo(expectedTopRight)
        assertThat(expectedBottomLeft).isEqualTo(resultBottomLeft)
        assertThat(expectedBottomRight).isEqualTo(resultBottomRight)
    }

    @Test
    fun `offsetToFocus should calculate focus`() = runTest {
        // given
        val middlePointX = 540f
        val middlePointY = 360f
        val offsetTopLeft = -middlePointX to -middlePointY
        val offsetTopRight = middlePointX to -middlePointY
        val offsetBottomLeft = -middlePointX to middlePointY
        val offsetBottomRight = middlePointX to middlePointY
        val expectedFocusTopLeft = -1f to 1f
        val expectedFocusTopRight = 1f to 1f
        val expectedFocusBottomLeft = -1f to -1f
        val expectedFocusBottomRight = 1f to -1f
        // when
        val resultTopLeft = offsetTopLeft.offsetToFocus(middlePointX, middlePointY)
        val resultTopRight = offsetTopRight.offsetToFocus(middlePointX, middlePointY)
        val resultBottomLeft = offsetBottomLeft.offsetToFocus(middlePointX, middlePointY)
        val resultBottomRight = offsetBottomRight.offsetToFocus(middlePointX, middlePointY)
        // then
        assertThat(resultTopLeft).isEqualTo(expectedFocusTopLeft)
        assertThat(resultTopRight).isEqualTo(expectedFocusTopRight)
        assertThat(resultBottomLeft).isEqualTo(expectedFocusBottomLeft)
        assertThat(resultBottomRight).isEqualTo(expectedFocusBottomRight)
    }

    @Test
    fun `normalizeUrl should return url`() = runTest {
        assertThat("test.ru".toNormalizedUrl()).startsWith("https")
    }

    @Test
    fun `isValidUrl returns true for valid url`() = runTest {
        assertThat("test.ru".isValidUrl()).isEqualTo(true)
        assertThat("abc.def.com".isValidUrl()).isEqualTo(true)
        assertThat("http://google.com".isValidUrl()).isEqualTo(true)
        assertThat("https://mastodon.social".isValidUrl()).isEqualTo(true)
        assertThat("https://mastodon.social/a/b/c".isValidUrl()).isEqualTo(true)
        assertThat("http://x.ru".isValidUrl()).isEqualTo(true)
    }

    @Test
    fun `isValidUrl returns false for invalid url`() = runTest {
        assertThat("test".isValidUrl()).isEqualTo(false)
        assertThat(" ".isValidUrl()).isEqualTo(false)
        assertThat("http:/".isValidUrl()).isEqualTo(false)
        assertThat("http://".isValidUrl()).isEqualTo(false)
        assertThat("http://x".isValidUrl()).isEqualTo(false)
        assertThat("a b c".isValidUrl()).isEqualTo(false)
        assertThat("test..ru".isValidUrl()).isEqualTo(false)
    }

    @Test
    fun `toDurationFormat returns formatted string`() = runTest {
        // given
        val x1 = 8
        val x2 = 53
        // when
        val result1 = x1.toDurationFormat()
        val result2 = x2.toDurationFormat()
        // then
        assertThat(result1).isEqualTo("08")
        assertThat(result2).isEqualTo("53")
    }

    @Test
    fun `toHumanReadableDuration should return formatted string`() = runTest {
        // given
        val x1 = 58.33f // 58 seconds
        val x2 = 62.01f // 1 min 2 seconds
        val x3 = 141.54f // 2 min 21 seconds
        val x4 = 778.54f // 12 min 58 seconds
        val x5 = 3601.02f // 1 hour 0 min 1 second
        val x6 = 3662.15f // 1 hour 1 min 2 second
        val x7 = 6323.0004f // 1 hour 45 min 23 sec
        // when
        val result1 = x1.toVideoDuration()
        val result2 = x2.toVideoDuration()
        val result3 = x3.toVideoDuration()
        val result4 = x4.toVideoDuration()
        val result5 = x5.toVideoDuration()
        val result6 = x6.toVideoDuration()
        val result7 = x7.toVideoDuration()
        // then
        assertThat(result1).isEqualTo("58")
        assertThat(result2).isEqualTo("01:02")
        assertThat(result3).isEqualTo("02:21")
        assertThat(result4).isEqualTo("12:58")
        assertThat(result5).isEqualTo("01:00:01")
        assertThat(result6).isEqualTo("01:01:02")
        assertThat(result7).isEqualTo("01:45:23")
    }
}
