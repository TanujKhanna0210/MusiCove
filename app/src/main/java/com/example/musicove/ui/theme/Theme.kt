package com.example.musicove.ui.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import com.dev.headphoneplayer.ui.theme.BackGroundColor
import com.dev.headphoneplayer.ui.theme.Black1
import com.dev.headphoneplayer.ui.theme.Black2
import com.dev.headphoneplayer.ui.theme.Black3
import com.dev.headphoneplayer.ui.theme.Gray200
import com.dev.headphoneplayer.ui.theme.Gray300
import com.dev.headphoneplayer.ui.theme.RedErrorDark
import com.dev.headphoneplayer.ui.theme.RedErrorLight
import com.dev.headphoneplayer.ui.theme.SurfaceColor
import com.dev.headphoneplayer.ui.theme.Teal200
import com.dev.headphoneplayer.ui.theme.TealA700
import com.dev.headphoneplayer.ui.theme.White
import com.dev.headphoneplayer.ui.theme.onBackGround
import com.dev.headphoneplayer.ui.theme.onSurface

@SuppressLint("ConflictingOnColor")
private val DarkColorPalette = darkColorScheme(
    primary = Teal200,
    onPrimary = White,
    primaryContainer = TealA700,
    secondary = Teal200,
    error = RedErrorLight,
    background = Black2,
    onBackground = Gray300,
    surface = Black3,
    onSurface = Gray200,
)

@SuppressLint("ConflictingOnColor")
private val LightColorPalette = lightColorScheme(
    primary = Teal200,
    onPrimary = White,
    primaryContainer = TealA700,
    secondary = Teal200,
    onSecondary = Black1,
    error = RedErrorDark,
    onError = RedErrorLight,
    background = BackGroundColor,
    onBackground = onBackGround,
    surface = SurfaceColor,
    onSurface = onSurface,
)

@Composable
fun MusiCoveTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}