package com.sedsoftware.tackle.compose.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.resources.ExperimentalResourceApi
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

@OptIn(ExperimentalResourceApi::class)
internal object TackleFonts {
    @Composable
    fun OpenSans() = FontFamily(
        Font(Res.font.open_sans_bold, FontWeight.Bold, FontStyle.Normal),
        Font(Res.font.open_sans_bold_italic, FontWeight.Bold, FontStyle.Italic),
        Font(Res.font.open_sans_italic, FontWeight.Normal, FontStyle.Italic),
        Font(Res.font.open_sans_light, FontWeight.Light, FontStyle.Normal),
        Font(Res.font.open_sans_light_italic, FontWeight.Light, FontStyle.Italic),
        Font(Res.font.open_sans_medium, FontWeight.Medium, FontStyle.Normal),
        Font(Res.font.open_sans_medium_italic, FontWeight.Medium, FontStyle.Italic),
        Font(Res.font.open_sans_regular, FontWeight.Normal, FontStyle.Normal),
    )
}
