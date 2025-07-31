package com.example.controlhorasmobile.ui.screens.informes.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import com.example.controlhorasmobile.ui.theme.AzulNoche
import com.example.controlhorasmobile.ui.theme.Blanco


@Composable
fun BotonFecha(
    label: String,
    fecha: LocalDate,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = AzulNoche,
            contentColor = Blanco
        ),
        modifier = Modifier
            .padding(horizontal = 8.dp)

    ) {
        Text(
            text = "$label: ${fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))}"
        )
    }
}