package com.example.cineexplorer.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColors = lightColorScheme(
    primary = Color(0xFF7D294E),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFD8E4),
    secondary = Color(0xFF76565F),
    secondaryContainer = Color(0xFFFFD9E1),
    background = Color(0xFFFFF8F8),
    surface = Color(0xFFFFF8F8)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFFFB0C8),
    onPrimary = Color(0xFF4A122D),
    primaryContainer = Color(0xFF641F40),
    secondary = Color(0xFFE5BDC6),
    secondaryContainer = Color(0xFF5C3F47),
    background = Color(0xFF181114),
    surface = Color(0xFF181114)
)

@Composable
fun CineExplorerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColors
        else -> LightColors
    }

    MaterialTheme(colorScheme = colorScheme, content = content)
}
