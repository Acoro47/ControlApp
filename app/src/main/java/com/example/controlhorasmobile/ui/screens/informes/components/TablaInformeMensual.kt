package com.example.controlhorasmobile.ui.screens.informes.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.controlhorasmobile.model.ResumenDia
import com.example.controlhorasmobile.ui.theme.AzulNoche
import com.example.controlhorasmobile.ui.theme.Blanco
import com.example.controlhorasmobile.ui.theme.ColorFinde
import com.example.controlhorasmobile.utils.capitalizar
import com.example.controlhorasmobile.utils.formatearMinutos

@Composable
fun TablaInformeMensual(
    resumenes: List<ResumenDia>,
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .background(
                Brush.verticalGradient(
                    colors = listOf(AzulNoche, MaterialTheme.colorScheme.background)
                )
            )

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(AzulNoche)
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            listOf("Dia", "Semana", "E1", "S1","E2", "S2","Extra").forEach {
                Text(
                    text = it,
                    color = Blanco,
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier
                        .weight(1f),
                    textAlign = TextAlign.Center
                )
            }
        }

        HorizontalDivider()

        resumenes.forEach { resumen ->
            val esFinde = resumen.diaSemana.lowercase() in listOf("sábado", "domingo")
            val fondo = if (esFinde) ColorFinde else Blanco
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(fondo)
                    .padding(vertical = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween

            ) {
                val extraMin = maxOf(0,resumen.totalMinutos - 240)

                listOf(
                    resumen.diaMes,
                    resumen.diaSemana.capitalizar(),
                    resumen.entrada1 ?: "-",
                    resumen.salida1 ?: "-",
                    resumen.entrada2 ?: "-",
                    resumen.salida2 ?: "-",
                    formatearMinutos(extraMin)
                ).forEach {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .weight(1f),
                        textAlign = TextAlign.Center
                    )
                }
            }
            HorizontalDivider()
        }
    }

}