package com.rescuemate.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.text.font.Font
import com.rescuemate.app.R


val poppinsFontFamily = FontFamily(
    Font(R.font.poppins_light, FontWeight.Light),
    Font(R.font.poppins_regular, FontWeight.Normal),
    Font(R.font.poppins_medium, FontWeight.Medium),
    Font(R.font.poppins_semi_bold, FontWeight.Bold),
)

// Default Material 3 typography values
val baseline = Typography()

val AppTypography = Typography(
    displayLarge = baseline.displayLarge.copy(fontFamily = poppinsFontFamily),
    displayMedium = baseline.displayMedium.copy(fontFamily = poppinsFontFamily),
    displaySmall = baseline.displaySmall.copy(fontFamily = poppinsFontFamily),
    headlineLarge = baseline.headlineLarge.copy(fontFamily = poppinsFontFamily),
    headlineMedium = baseline.headlineMedium.copy(fontFamily = poppinsFontFamily),
    headlineSmall = baseline.headlineSmall.copy(fontFamily = poppinsFontFamily),
    titleLarge = baseline.titleLarge.copy(fontFamily = poppinsFontFamily),
    titleMedium = baseline.titleMedium.copy(fontFamily = poppinsFontFamily),
    titleSmall = baseline.titleSmall.copy(fontFamily = poppinsFontFamily),
    bodyLarge = baseline.bodyLarge.copy(fontFamily = poppinsFontFamily),
    bodyMedium = baseline.bodyMedium.copy(fontFamily = poppinsFontFamily),
    bodySmall = baseline.bodySmall.copy(fontFamily = poppinsFontFamily),
    labelLarge = baseline.labelLarge.copy(fontFamily = poppinsFontFamily),
    labelMedium = baseline.labelMedium.copy(fontFamily = poppinsFontFamily),
    labelSmall = baseline.labelSmall.copy(fontFamily = poppinsFontFamily),
)