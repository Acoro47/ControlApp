package com.example.controlhorasmobile.ui.screens.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.example.controlhorasmobile.ui.theme.AzulNoche
import com.example.controlhorasmobile.ui.theme.Blanco

@Composable
fun CabeceraDashboardScreen(
    username: String,
    fechaActual: String,
    onCerrarSesionClick: () -> Unit,
    onInformesClick: () -> Unit,
    onBackClick: () -> Unit,
    mostrarBotonInformes: Boolean = true,
    mostrarBotonVolver: Boolean = false
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(AzulNoche, MaterialTheme.colorScheme.background),
                    startY = 0f,
                    endY = 600f
                )
            )
            .padding(
                horizontal = 16.dp,
                vertical = 24.dp
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Hola, ${username.uppercase()}",
                style = MaterialTheme.typography.titleLarge.copy(
                    color = Blanco
                )
            )
            Text(
                text = fechaActual,
                style = MaterialTheme.typography.titleLarge.copy(
                    color = Blanco
                )
            )
        }

        if (mostrarBotonInformes){
            Button(
                onClick = onInformesClick,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AzulNoche,
                    contentColor = Blanco
                ),
                modifier = Modifier
                    .height(40.dp)
            ) {
                Text("Informes")
            }
        }

        if (mostrarBotonVolver) {
            Button(
                onClick = onBackClick,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AzulNoche,
                    contentColor = Blanco
                ),
                modifier = Modifier
                    .height(40.dp)
            ) {
                Text(
                    text = "Volver",
                    color = Blanco
                )
            }
        }


        Button(
            onClick = onCerrarSesionClick,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AzulNoche,
                contentColor = Blanco
            ),
            modifier = Modifier
                .height(40.dp)
        ) {
            Text("Salir")
        }
    }
}