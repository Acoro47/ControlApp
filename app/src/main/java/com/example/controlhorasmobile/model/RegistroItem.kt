package com.example.controlhorasmobile.model

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.controlhorasmobile.ui.theme.AzulNoche
import com.example.controlhorasmobile.ui.theme.Blanco
import com.example.controlhorasmobile.ui.theme.VerdeBrisa

@Composable
fun RegistroItem(registro: Registro){

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = AzulNoche
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(AzulNoche, VerdeBrisa),
                        startY = 0f,
                        endY = 600f
                    )
                )
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "📅 Fecha: ${registro.fecha}",
                    style = MaterialTheme.typography.bodyMedium.copy(color = Blanco)
                )
                Text(
                    text = "⏱️ Entrada: ${registro.horaEntrada}",
                    style = MaterialTheme.typography.bodyMedium.copy(color = Blanco)
                )
                Text(
                    text = "⏱\uFE0F Salida: ${registro.horaSalida}",
                    style = MaterialTheme.typography.bodyMedium.copy(color = Blanco)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "⏱\uFE0F Duración: ${registro.duracion}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (registro.duracion.contains("En curso")) Color(0xFFff9800) else Blanco
                )

            }
        }

    }
}