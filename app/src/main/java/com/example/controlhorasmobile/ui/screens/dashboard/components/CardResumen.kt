package com.example.controlhorasmobile.ui.screens.dashboard.components


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Brush
import com.example.controlhorasmobile.ui.theme.AzulNoche
import com.example.controlhorasmobile.ui.theme.Blanco
import com.example.controlhorasmobile.ui.theme.VerdeBrisa

@Composable
fun CardResumen(
    diasTrabajados: Int,
    horasMes: String,
    diaLibre: String
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = AzulNoche),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(VerdeBrisa,AzulNoche),
                        startY = 0f,
                        endY = 600f
                    )
                )
                .padding(16.dp),
            contentAlignment = Alignment.Center

        ){
            Column(
                modifier = Modifier
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Resumen del mes",
                    style = MaterialTheme.typography.titleMedium.copy(color = Blanco)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Dias trabajados: $diasTrabajados",
                    style = MaterialTheme.typography.titleMedium.copy(color = Blanco)
                )
                Text(
                    text = "Horas totales: $horasMes",
                    style = MaterialTheme.typography.titleMedium.copy(color = Blanco)
                )
                Text(
                    text = "Día libre: $diaLibre",
                    style = MaterialTheme.typography.titleMedium.copy(color = Blanco)
                )
            }

        }

    }
}