package com.example.controlhorasmobile.ui.theme

import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF1CA67F)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val ColorPrimario = Color(0xFF1ABC9C)
val ColorSecundario = Color(0xFF0A2E47)

val GrisClaro = Color(0xFFB6B7B6)



val VerdeBrisa = Color(0xFF00B894)         // Color primario: energía y frescura
val AzulProfundo = Color(0xFF2D3436)       // Fondo oscuro elegante
val GrisNube = Color(0xFFF5F6F7)           // Fondo claro suave
val GrisCarbon = Color(0xFF2C3E50)         // Texto principal
val GrisMedianoche = Color(0xFF7F8C8D)     // Texto secundario
val VerdePastel = Color(0xFFA3E4D7)        // Indicadores positivos
val RojoCoral = Color(0xFFE74C3C)          // Indicadores de error
val AzulNoche = Color(0xFF1A237E)          // Fondo en modo oscuro
val Blanco = Color(0xFFFFFFFF)             // Texto sobre fondos oscuros
val ColorFinde = Color(red = 211, green = 124, blue = 211)


@Composable
fun customTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedTrailingIconColor = ColorSecundario,
    unfocusedTrailingIconColor = ColorSecundario,
    errorTrailingIconColor = Color.Red,
    disabledTextColor = GrisClaro,
    focusedBorderColor = ColorPrimario,
    unfocusedBorderColor = ColorSecundario,
    cursorColor = Color.Black,
    focusedLabelColor = ColorSecundario,
    unfocusedLabelColor = ColorPrimario,


)
