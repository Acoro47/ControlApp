package com.example.controlhorasmobile.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val colorSchemeClaro = lightColorScheme(
    primary = VerdeBrisa,
    onPrimary = Blanco,
    background = GrisNube,
    onBackground = GrisCarbon,
    surface = Blanco,
    onSurface = GrisCarbon,
    error = RojoCoral,
    onError = Blanco,

)

private val colorSchemeOscuro = darkColorScheme(
    primary = VerdeBrisa,
    onPrimary = Blanco,
    background = AzulProfundo,
    onBackground = Blanco,
    surface = AzulNoche,
    onSurface = GrisNube,
    error = RojoCoral,
    onError = Blanco,

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun ControlHorasTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
){
    MaterialTheme(
        colorScheme = if (darkTheme) colorSchemeOscuro else colorSchemeClaro,
        typography = Typography,
        content = content
    )
}