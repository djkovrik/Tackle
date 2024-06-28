package com.sedsoftware.tackle.compose.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import com.seiko.imageloader.ImageLoader
import com.seiko.imageloader.LocalImageLoader
import com.seiko.imageloader.component.setupDefaultComponents
import com.seiko.imageloader.intercept.bitmapMemoryCacheConfig

internal val LocalThemeIsDark = compositionLocalOf { mutableStateOf(true) }

@Composable
fun TackleTheme(
    systemIsDark: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {

    val isDarkState = remember { mutableStateOf(systemIsDark) }

    CompositionLocalProvider(
        LocalThemeIsDark provides isDarkState
    ) {
        val isDark by isDarkState
        SystemAppearance(!isDark)
        MaterialTheme(
            colorScheme = if (isDark) DarkColorScheme else LightColorScheme,
            typography = TackleFonts.Typography(),
            shapes = Shapes(
                extraSmall = RoundedCornerShape(4.dp),
                small = RoundedCornerShape(6.dp),
                medium = RoundedCornerShape(8.dp),
                large = RoundedCornerShape(10.dp),
                extraLarge = RoundedCornerShape(12.dp)
            ),
            content = {
                Surface(color = MaterialTheme.colorScheme.background) {
                    content()
                }
            }
        )
    }
}

@Composable
internal fun TackleScreenPreview(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit,
) {
    TackleTheme(systemIsDark = darkTheme) {
        CompositionLocalProvider(
            LocalImageLoader provides generatePreviewImageLoader()
        ) {
            content()
        }
    }
}

private fun generatePreviewImageLoader(): ImageLoader {
    return ImageLoader {
        components {
            setupDefaultComponents()
        }
        interceptor {
            bitmapMemoryCacheConfig {
                maxSize(32 * 1024 * 1024) // 32MB
            }
        }
    }
}
