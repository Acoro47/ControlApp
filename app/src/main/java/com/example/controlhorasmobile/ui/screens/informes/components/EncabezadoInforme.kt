package com.example.controlhorasmobile.ui.screens.informes.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.controlhorasmobile.ui.theme.Blanco

@Composable
fun EncabezadoInforme(
    fechaFormateada: String
) {
    Column {
        Text(
            text = "Informe mensual",
            style = MaterialTheme.typography.titleLarge.copy(color = Blanco),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))
        HorizontalDivider(color = Blanco.copy(alpha = 0.3f))
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = fechaFormateada,
            style = MaterialTheme.typography.labelMedium.copy(color = Blanco),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}