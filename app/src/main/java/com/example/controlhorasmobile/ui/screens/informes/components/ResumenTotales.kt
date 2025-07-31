package com.example.controlhorasmobile.ui.screens.informes.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ResumenTotales(
    horasLaborables: Int,
    horasFindes: Int,
    tarifaLaborable: Double,
    tarifaFinde: Double,
    importeLaborable: Double,
    importeFinde: Double,
    totalEuros: Double
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .padding(12.dp)
    ) {
        Text(
            text = "Resumen del mes",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text("Horas laborales: ${horasLaborables / 60} h ${horasLaborables % 60} m")
        Text("Horas fin de semana: ${horasFindes / 60} h ${horasFindes % 60} m")
        Text("Tarifa por hora extra laborables: %.2f €/h".format(tarifaLaborable))
        Text("Tarifa por hora extra fin de semana: %.2f €/h".format(tarifaFinde))
        Text("Importe días laborales: %.2f €".format(importeLaborable))
        Text("Importe días festivos: %.2f €".format(importeFinde))
        Text("Total a pagar: %.2f €".format(totalEuros))
    }
}