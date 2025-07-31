package com.example.controlhorasmobile.ui.screens.informes.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.controlhorasmobile.ui.theme.AzulNoche
import com.example.controlhorasmobile.ui.theme.Blanco

@Composable
fun AccionesInforme(
    onSeleccionarMes: () -> Unit,
    onExportar: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween

    ) {
        Button(
            onClick = onSeleccionarMes,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AzulNoche,
                contentColor = Blanco
            ),
            modifier = Modifier
                .padding(horizontal = 8.dp)
        ) {
            Text("Seleccionar mes")
        }

        Button(
            onClick = onExportar,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AzulNoche,
                contentColor = Blanco
            ),
            modifier = Modifier
                .padding(horizontal = 8.dp)
        ) {
            Text("Extraer en pdf")
        }
    }
}