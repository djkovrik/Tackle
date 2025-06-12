package com.sedsoftware.tackle.compose.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.Font
import tackle.shared.compose.generated.resources.Res
import tackle.shared.compose.generated.resources.open_sans_bold
import tackle.shared.compose.generated.resources.open_sans_bold_italic
import tackle.shared.compose.generated.resources.open_sans_italic
import tackle.shared.compose.generated.resources.open_sans_light
import tackle.shared.compose.generated.resources.open_sans_light_italic
import tackle.shared.compose.generated.resources.open_sans_medium
import tackle.shared.compose.generated.resources.open_sans_medium_italic
import tackle.shared.compose.generated.resources.open_sans_regular
import tackle.shared.compose.generated.resources.open_sans_semibold
import tackle.shared.compose.generated.resources.open_sans_semibold_italic

internal object TackleFonts {

    @Composable
    fun OpenSans() = FontFamily(
        Font(Res.font.open_sans_bold, FontWeight.Bold, FontStyle.Normal),
        Font(Res.font.open_sans_bold_italic, FontWeight.Bold, FontStyle.Italic),
        Font(Res.font.open_sans_medium, FontWeight.Medium, FontStyle.Normal),
        Font(Res.font.open_sans_medium_italic, FontWeight.Medium, FontStyle.Italic),
        Font(Res.font.open_sans_regular, FontWeight.Normal, FontStyle.Normal),
        Font(Res.font.open_sans_italic, FontWeight.Normal, FontStyle.Italic),
        Font(Res.font.open_sans_light, FontWeight.Light, FontStyle.Normal),
        Font(Res.font.open_sans_light_italic, FontWeight.Light, FontStyle.Italic),
        Font(Res.font.open_sans_semibold, FontWeight.SemiBold, FontStyle.Normal),
        Font(Res.font.open_sans_semibold_italic, FontWeight.SemiBold, FontStyle.Italic),
    )

    @Composable
    @Suppress("LongMethod")
    fun Typography(): Typography =
        Typography(
            headlineLarge = TextStyle(
                fontFamily = OpenSans(),
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Normal,
                fontSize = 40.sp,
                lineHeight = 48.sp,
                letterSpacing = 0.0.sp,
            ),
            headlineMedium = TextStyle(
                fontFamily = OpenSans(),
                fontWeight = FontWeight.Medium,
                fontStyle = FontStyle.Normal,
                fontSize = 36.sp,
                lineHeight = 44.sp,
                letterSpacing = 0.0.sp,
            ),
            headlineSmall = TextStyle(
                fontFamily = OpenSans(),
                fontWeight = FontWeight.Normal,
                fontStyle = FontStyle.Normal,
                fontSize = 32.sp,
                lineHeight = 40.sp,
                letterSpacing = 0.0.sp,
            ),
            titleLarge = TextStyle(
                fontFamily = OpenSans(),
                fontWeight = FontWeight.Medium,
                fontStyle = FontStyle.Normal,
                fontSize = 28.sp,
                lineHeight = 34.sp,
                letterSpacing = 0.0.sp,
            ),
            titleMedium = TextStyle(
                fontFamily = OpenSans(),
                fontWeight = FontWeight.Normal,
                fontStyle = FontStyle.Normal,
                fontSize = 24.sp,
                lineHeight = 30.sp,
                letterSpacing = 0.2.sp,
            ),
            titleSmall = TextStyle(
                fontFamily = OpenSans(),
                fontWeight = FontWeight.Normal,
                fontStyle = FontStyle.Normal,
                fontSize = 20.sp,
                lineHeight = 26.sp,
                letterSpacing = 0.1.sp,
            ),
            bodyLarge = TextStyle(
                fontFamily = OpenSans(),
                fontWeight = FontWeight.Normal,
                fontStyle = FontStyle.Normal,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.5.sp,
            ),
            bodyMedium = TextStyle(
                fontFamily = OpenSans(),
                fontWeight = FontWeight.Normal,
                fontStyle = FontStyle.Normal,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                letterSpacing = 0.2.sp,
            ),
            bodySmall = TextStyle(
                fontFamily = OpenSans(),
                fontWeight = FontWeight.Normal,
                fontStyle = FontStyle.Normal,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                letterSpacing = 0.4.sp,
            ),
            labelLarge = TextStyle(
                fontFamily = OpenSans(),
                fontWeight = FontWeight.Medium,
                fontStyle = FontStyle.Normal,
                fontSize = 16.sp,
                lineHeight = 20.sp,
                letterSpacing = 0.1.sp,
            ),
            labelMedium = TextStyle(
                fontFamily = OpenSans(),
                fontWeight = FontWeight.Normal,
                fontStyle = FontStyle.Normal,
                fontSize = 14.sp,
                lineHeight = 18.sp,
                letterSpacing = 0.5.sp,
            ),
            labelSmall = TextStyle(
                fontFamily = OpenSans(),
                fontWeight = FontWeight.Normal,
                fontStyle = FontStyle.Italic,
                fontSize = 14.sp,
                lineHeight = 18.sp,
                letterSpacing = 0.5.sp,
            )
        )
}
