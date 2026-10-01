package com.example.ui.theme

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

private val DarkColorScheme = darkColorScheme(
    primary = LearnzoIndigoLight,
    secondary = LearnzoGold,
    tertiary = LearnzoCyan,
    background = Color(0xFF141221),
    surface = Color(0xFF201D34),
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onTertiary = Color.Black,
    onBackground = Color(0xFFECEBFA),
    onSurface = Color(0xFFECEBFA)
)

private val LightColorScheme = lightColorScheme(
    primary = LearnzoIndigo,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFEADBFF),
    onPrimaryContainer = Color(0xFF22005D),
    secondary = LearnzoGold,
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFFFFE082),
    onSecondaryContainer = Color(0xFF281800),
    tertiary = LearnzoMint,
    onTertiary = Color.White,
    background = LearnzoBgLight,
    surface = LearnzoSurface,
    onBackground = LearnzoTextPrimary,
    onSurface = LearnzoTextPrimary,
    surfaceVariant = Color(0xFFF0F3FA),
    onSurfaceVariant = LearnzoTextSecondary
)

@Composable
fun LearnzoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    LearnzoTheme(darkTheme = darkTheme, dynamicColor = dynamicColor, content = content)
}
