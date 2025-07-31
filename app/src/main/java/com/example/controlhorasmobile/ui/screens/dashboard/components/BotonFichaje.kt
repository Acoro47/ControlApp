package com.example.controlhorasmobile.ui.screens.dashboard.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.controlhorasmobile.ui.theme.Blanco
import com.example.controlhorasmobile.ui.theme.VerdeBrisa


@Composable
fun BotonFichaje(
    texto: String,
    onClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    colorTexto: Color = VerdeBrisa
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .height(56.dp)
            .border(
                1.dp,
                VerdeBrisa,
                shape = MaterialTheme.shapes.medium
            ),
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.buttonColors(
            contentColor = colorTexto,
            containerColor = Blanco
        )

    ) {
        Text(texto)
    }

}
