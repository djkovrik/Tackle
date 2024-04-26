package com.sedsoftware.tackle.compose.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// Palette from
// https://colorhunt.co/palette/401f71824d74be7b72fdaf7b
// and https://m3.material.io/theme-builder#/custom

internal val md_theme_light_primary = Color(0xFF8E437D)
internal val md_theme_light_onPrimary = Color(0xFFFFFFFF)
internal val md_theme_light_primaryContainer = Color(0xFFFFD7F0)
internal val md_theme_light_onPrimaryContainer = Color(0xFF3A0032)
internal val md_theme_light_secondary = Color(0xFF9C4238)
internal val md_theme_light_onSecondary = Color(0xFFFFFFFF)
internal val md_theme_light_secondaryContainer = Color(0xFFFFDAD5)
internal val md_theme_light_onSecondaryContainer = Color(0xFF410001)
internal val md_theme_light_tertiary = Color(0xFF954A05)
internal val md_theme_light_onTertiary = Color(0xFFFFFFFF)
internal val md_theme_light_tertiaryContainer = Color(0xFFFFDCC7)
internal val md_theme_light_onTertiaryContainer = Color(0xFF311300)
internal val md_theme_light_error = Color(0xFFBA1A1A)
internal val md_theme_light_errorContainer = Color(0xFFFFDAD6)
internal val md_theme_light_onError = Color(0xFFFFFFFF)
internal val md_theme_light_onErrorContainer = Color(0xFF410002)
internal val md_theme_light_background = Color(0xFFFAFAFA)
internal val md_theme_light_onBackground = Color(0xFF1F1A1D)
internal val md_theme_light_surface = Color(0xFFFAFAFA)
internal val md_theme_light_onSurface = Color(0xFF1F1A1D)
internal val md_theme_light_surfaceVariant = Color(0xFFEFDEE6)
internal val md_theme_light_onSurfaceVariant = Color(0xFF4F444A)
internal val md_theme_light_outline = Color(0xFF81737B)
internal val md_theme_light_inverseOnSurface = Color(0xFFFAFAFA)
internal val md_theme_light_inverseSurface = Color(0xFF342F32)
internal val md_theme_light_inversePrimary = Color(0xFFFFACE7)
internal val md_theme_light_surfaceTint = Color(0xFF8E437D)
internal val md_theme_light_outlineVariant = Color(0xFFD2C2CA)
internal val md_theme_light_scrim = Color(0xFF000000)

internal val md_theme_dark_primary = Color(0xFFFFACE7)
internal val md_theme_dark_onPrimary = Color(0xFF57124C)
internal val md_theme_dark_primaryContainer = Color(0xFF722B64)
internal val md_theme_dark_onPrimaryContainer = Color(0xFFFFD7F0)
internal val md_theme_dark_secondary = Color(0xFFFFB4A9)
internal val md_theme_dark_onSecondary = Color(0xFF5F150F)
internal val md_theme_dark_secondaryContainer = Color(0xFF7D2B23)
internal val md_theme_dark_onSecondaryContainer = Color(0xFFFFDAD5)
internal val md_theme_dark_tertiary = Color(0xFFFFB787)
internal val md_theme_dark_onTertiary = Color(0xFF502400)
internal val md_theme_dark_tertiaryContainer = Color(0xFF723600)
internal val md_theme_dark_onTertiaryContainer = Color(0xFFFFDCC7)
internal val md_theme_dark_error = Color(0xFFFFB4AB)
internal val md_theme_dark_errorContainer = Color(0xFF93000A)
internal val md_theme_dark_onError = Color(0xFF690005)
internal val md_theme_dark_onErrorContainer = Color(0xFFFFDAD6)
internal val md_theme_dark_background = Color(0xFF1F1A1D)
internal val md_theme_dark_onBackground = Color(0xFFEAE0E3)
internal val md_theme_dark_surface = Color(0xFF1F1A1D)
internal val md_theme_dark_onSurface = Color(0xFFEAE0E3)
internal val md_theme_dark_surfaceVariant = Color(0xFF4F444A)
internal val md_theme_dark_onSurfaceVariant = Color(0xFFD2C2CA)
internal val md_theme_dark_outline = Color(0xFF9B8D94)
internal val md_theme_dark_inverseOnSurface = Color(0xFF1F1A1D)
internal val md_theme_dark_inverseSurface = Color(0xFFEAE0E3)
internal val md_theme_dark_inversePrimary = Color(0xFF8E437D)
internal val md_theme_dark_surfaceTint = Color(0xFFFFACE7)
internal val md_theme_dark_outlineVariant = Color(0xFF4F444A)
internal val md_theme_dark_scrim = Color(0xFF000000)

internal val LightColorScheme = lightColorScheme(
    primary = md_theme_light_primary,
    onPrimary = md_theme_light_onPrimary,
    primaryContainer = md_theme_light_primaryContainer,
    onPrimaryContainer = md_theme_light_onPrimaryContainer,
    secondary = md_theme_light_secondary,
    onSecondary = md_theme_light_onSecondary,
    secondaryContainer = md_theme_light_secondaryContainer,
    onSecondaryContainer = md_theme_light_onSecondaryContainer,
    tertiary = md_theme_light_tertiary,
    onTertiary = md_theme_light_onTertiary,
    tertiaryContainer = md_theme_light_tertiaryContainer,
    onTertiaryContainer = md_theme_light_onTertiaryContainer,
    error = md_theme_light_error,
    errorContainer = md_theme_light_errorContainer,
    onError = md_theme_light_onError,
    onErrorContainer = md_theme_light_onErrorContainer,
    background = md_theme_light_background,
    onBackground = md_theme_light_onBackground,
    surface = md_theme_light_surface,
    onSurface = md_theme_light_onSurface,
    surfaceVariant = md_theme_light_surfaceVariant,
    onSurfaceVariant = md_theme_light_onSurfaceVariant,
    outline = md_theme_light_outline,
    inverseOnSurface = md_theme_light_inverseOnSurface,
    inverseSurface = md_theme_light_inverseSurface,
    inversePrimary = md_theme_light_inversePrimary,
    surfaceTint = md_theme_light_surfaceTint,
    outlineVariant = md_theme_light_outlineVariant,
    scrim = md_theme_light_scrim,
)

internal val DarkColorScheme = darkColorScheme(
    primary = md_theme_dark_primary,
    onPrimary = md_theme_dark_onPrimary,
    primaryContainer = md_theme_dark_primaryContainer,
    onPrimaryContainer = md_theme_dark_onPrimaryContainer,
    secondary = md_theme_dark_secondary,
    onSecondary = md_theme_dark_onSecondary,
    secondaryContainer = md_theme_dark_secondaryContainer,
    onSecondaryContainer = md_theme_dark_onSecondaryContainer,
    tertiary = md_theme_dark_tertiary,
    onTertiary = md_theme_dark_onTertiary,
    tertiaryContainer = md_theme_dark_tertiaryContainer,
    onTertiaryContainer = md_theme_dark_onTertiaryContainer,
    error = md_theme_dark_error,
    errorContainer = md_theme_dark_errorContainer,
    onError = md_theme_dark_onError,
    onErrorContainer = md_theme_dark_onErrorContainer,
    background = md_theme_dark_background,
    onBackground = md_theme_dark_onBackground,
    surface = md_theme_dark_surface,
    onSurface = md_theme_dark_onSurface,
    surfaceVariant = md_theme_dark_surfaceVariant,
    onSurfaceVariant = md_theme_dark_onSurfaceVariant,
    outline = md_theme_dark_outline,
    inverseOnSurface = md_theme_dark_inverseOnSurface,
    inverseSurface = md_theme_dark_inverseSurface,
    inversePrimary = md_theme_dark_inversePrimary,
    surfaceTint = md_theme_dark_surfaceTint,
    outlineVariant = md_theme_dark_outlineVariant,
    scrim = md_theme_dark_scrim,
)
