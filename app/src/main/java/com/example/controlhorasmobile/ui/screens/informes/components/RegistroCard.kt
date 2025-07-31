package com.example.controlhorasmobile.ui.screens.informes.components


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import com.example.controlhorasmobile.model.ResumenDia
import com.example.controlhorasmobile.ui.theme.AzulNoche
import com.example.controlhorasmobile.ui.theme.Blanco
import com.example.controlhorasmobile.ui.theme.VerdeBrisa
import com.example.controlhorasmobile.utils.capitalizar

@Composable
fun RegistroCard(
    resumen: ResumenDia
) {

    val diaSemana = resumen.diaSemana.capitalizar()
    val finde = diaSemana == "Sábado" || diaSemana == "Domingo"

    val fondo = if (finde) {
        Brush.verticalGradient(listOf(Color(0xFFE3F2FD), Color(0xFFC8E6C9)))
    }
    else {
        Brush.verticalGradient(listOf(AzulNoche, VerdeBrisa))
    }
    val colorTexto = if (finde) Color(0xFF212121) else Blanco

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(fondo)
                .padding(16.dp),
            contentAlignment = Alignment.Center

        ){
            Column(
                modifier = Modifier.padding(6.dp)

            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {

                    Text(
                        text = diaSemana,
                        style = MaterialTheme.typography.labelMedium.copy(color = colorTexto)
                    )

                    Text(
                        text = " " + resumen.diaMes,
                        style = MaterialTheme.typography.labelMedium.copy(color = colorTexto)
                    )

                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text ="Entrada: ",
                        style = MaterialTheme.typography.labelMedium.copy(color = colorTexto)
                    )
                    Text(
                        text = resumen.entrada1 ?: "—",
                        style = MaterialTheme.typography.labelMedium.copy(color = colorTexto)
                    )
                    Text(
                        text = " Salida: ",
                        style = MaterialTheme.typography.labelMedium.copy(color = colorTexto)
                    )
                    Text(
                        text = resumen.salida1 ?: "—",
                        style = MaterialTheme.typography.labelMedium.copy(color = colorTexto)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    if (!resumen.entrada2.isNullOrBlank() && !resumen.salida2.isNullOrBlank()){
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {

                            Text(
                                text = "Entrada: ",
                                style = MaterialTheme.typography.labelMedium.copy(color = colorTexto)
                            )
                            Text(
                                text = resumen.entrada2,
                                style = MaterialTheme.typography.labelMedium.copy(color = colorTexto)
                            )
                            Text(
                                text = " Salida: ",
                                style = MaterialTheme.typography.labelMedium.copy(color = colorTexto)
                            )
                            Text(
                                text = resumen.salida2,
                                style = MaterialTheme.typography.labelMedium.copy(color = colorTexto)
                            )
                        }
                    }

                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Total horas: ",
                        style = MaterialTheme.typography.labelMedium.copy(color = colorTexto)
                    )
                    Text(
                        text = resumen.duracion ?: " —",
                        style = MaterialTheme.typography.labelMedium.copy(color = colorTexto)
                    )
                }
            }
        }
    }
}