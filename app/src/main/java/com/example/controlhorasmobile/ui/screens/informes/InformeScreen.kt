package com.example.controlhorasmobile.ui.screens.informes

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.controlhorasmobile.ui.screens.informes.components.BotonFecha
import org.threeten.bp.LocalDate
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextAlign
import com.example.controlhorasmobile.ui.screens.dashboard.CabeceraDashboardScreen
import com.example.controlhorasmobile.ui.screens.informes.components.RegistroCard
import com.example.controlhorasmobile.ui.screens.informes.components.mostrarDatePicker
import com.example.controlhorasmobile.ui.theme.AzulNoche
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.navigation.NavController
import com.example.controlhorasmobile.model.ResumenDia
import com.example.controlhorasmobile.ui.screens.informes.components.BotonInforme
import com.example.controlhorasmobile.ui.screens.informes.logic.resumenPeriodo
import com.example.controlhorasmobile.ui.theme.Blanco
import com.example.controlhorasmobile.utils.cerrarSesion


@Composable
fun InformeScreen(
    idUsuario: Long,
    modifier: Modifier = Modifier,
    navController: NavController,
)

{
    val context = LocalContext.current
    val fechaInicio = remember { mutableStateOf(LocalDate.now().withDayOfMonth(1)) }
    val fechaFin = remember { mutableStateOf(LocalDate.now()) }

    val prefs = context.getSharedPreferences("usuario", Context.MODE_PRIVATE)
    val username = prefs.getString("username", "Usuario") ?: "--"
    val token = prefs.getString("token", "") ?: ""

    val formato = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val fechaFormato = formato.format(Date())

    val resumenes = remember { mutableStateListOf<ResumenDia>() }

    LaunchedEffect(fechaInicio.value, fechaFin.value) {
        //val nuevos = resumenPeriodo(token,idUsuario, fechaInicio.value, fechaFin.value, context)
        resumenes.clear()
        //resumenes.addAll(nuevos)
    }


    val minutosTotales = resumenes.sumOf { it.totalMinutos}
    val resumenFormateado = "${minutosTotales / 60} h ${minutosTotales % 60 } m"

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CabeceraDashboardScreen(
                username = username,
                fechaActual = fechaFormato,
                onCerrarSesionClick = {
                    cerrarSesion(navController, prefs)
                },
                onInformesClick = {
                    navController.navigate("informes")
                },
                onBackClick = {
                    navController.popBackStack()
                },
                mostrarBotonInformes = false,
                mostrarBotonVolver = true
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(AzulNoche, MaterialTheme.colorScheme.background),
                            startY = 0f,
                            endY = 600f
                        )
                    )
                    .navigationBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)

            ) {
                Text(
                    text = "Total del periodo seleccionado: $resumenFormateado",
                    style = MaterialTheme.typography.titleMedium,
                    color = Blanco
                )
                BotonInforme(
                    label = "Generar Informe",
                    onClick = {
                        navController.navigate("informeMensual")
                    }
                )
            }

        }
    ) { innerPadding ->

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(AzulNoche, MaterialTheme.colorScheme.background)
                    )
                )
                .padding(16.dp)
        ) {
            Text(
                text = "Informes de actividad",
                style = MaterialTheme.typography.titleLarge.copy(color = Blanco),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                BotonFecha(
                    label = "Inicio",
                    fecha = fechaInicio.value,
                    onClick = {
                        mostrarDatePicker(context, fechaInicio.value) {
                            fechaInicio.value = it
                        }
                    }
                )
                BotonFecha(
                    label = "Fin",
                    fecha = fechaFin.value,
                    onClick = {
                        mostrarDatePicker(context, fechaFin.value) {
                            fechaFin.value = it
                        }
                    }
                )
            }


            Spacer(modifier = Modifier.height(4.dp))

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(resumenes) { resumen ->
                    RegistroCard(resumen)
                    HorizontalDivider()
                }
            }
        }
    }
}