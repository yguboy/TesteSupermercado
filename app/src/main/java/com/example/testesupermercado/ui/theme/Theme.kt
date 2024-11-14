package com.example.testesupermercado.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val CustomColorScheme = lightColorScheme(
    primary = Color(0xFF6200EE),
    onPrimary = Color.White,
    secondary = Color(0xFF03DAC6),
    onSecondary = Color.Black,
    surface = Color.White,
    onSurface = Color.Black,
)

val CustomTypography = Typography(
    headlineLarge = TextStyle(fontSize = 30.sp, fontWeight = FontWeight.Bold),  // h1
    headlineMedium = TextStyle(fontSize = 24.sp), // h2
    headlineSmall = TextStyle(fontSize = 20.sp), // h3
    bodyLarge = TextStyle(fontSize = 16.sp), // body1
    bodyMedium = TextStyle(fontSize = 14.sp), // body2
)

@Composable
fun SupermercadoTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = CustomColorScheme,
        typography = CustomTypography,
        content = content
    )
}
