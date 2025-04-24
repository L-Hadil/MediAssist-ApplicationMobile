package com.mediassist.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


private val LightColors = lightColorScheme(
    primary = Color(0xFF6EC6FF),
    onPrimary = Color(0xFF003C8F),

    secondary = Color(0xFF82B1FF),
    onSecondary = Color(0xFF0D47A1),

    background = Color(0xFFE3F2FD),
    onBackground = Color(0xFF0D47A1),

    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF0D47A1),

    error = Color(0xFFD32F2F),
    onError = Color.White
)

@Composable
fun MediAssistTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        typography = Typography(),
        content = content
    )
}
