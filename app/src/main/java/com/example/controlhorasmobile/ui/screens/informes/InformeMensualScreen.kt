package com.example.controlhorasmobile.ui.screens.informes

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.controlhorasmobile.PREFS_NAME
import com.example.controlhorasmobile.model.Registro
import com.example.controlhorasmobile.model.ResumenDia
import com.example.controlhorasmobile.model.dto.toRegistro
import com.example.controlhorasmobile.network.RetrofitClient
import com.example.controlhorasmobile.network.UsuarioService
import com.example.controlhorasmobile.ui.screens.dashboard.CabeceraDashboardScreen
import com.example.controlhorasmobile.ui.screens.dashboard.logic.DashboardCalculations.toIsoString
import com.example.controlhorasmobile.ui.screens.informes.components.AccionesInforme
import com.example.controlhorasmobile.ui.screens.informes.components.EncabezadoInforme
import com.example.controlhorasmobile.ui.screens.informes.components.ResumenTotales
import com.example.controlhorasmobile.ui.screens.informes.components.TablaInformeMensual
import com.example.controlhorasmobile.ui.screens.informes.components.mostrarFechaPicker
import com.example.controlhorasmobile.ui.screens.informes.logic.registrosDia
import com.example.controlhorasmobile.ui.screens.informes.logic.rememberPdfSaver
import com.example.controlhorasmobile.ui.theme.AzulNoche
import com.example.controlhorasmobile.ui.theme.Blanco
import com.example.controlhorasmobile.ui.viewModels.InformeUiState
import com.example.controlhorasmobile.ui.viewModels.InformeViewModel
import com.example.controlhorasmobile.utils.capitalizar
import com.example.controlhorasmobile.utils.cerrarSesion
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth
import org.threeten.bp.format.DateTimeFormatter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun InformePreviewScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val mesSeleccionado = remember { mutableStateOf(LocalDate.now().withDayOfMonth(1)) }
    val tarifaExtra = remember { mutableDoubleStateOf(9.0) }
    val resumenMensual = remember { mutableStateListOf<ResumenDia>() }

    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    val username = prefs.getString("username", "Usuario") ?: "--"
    val token = prefs.getString("TOKEN_KEY","")
    val idUsuario = prefs.getLong("idUsuario",-1)
    val formato = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val fechaFormato = formato.format(Date())

    val formatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale("es","ES"))
    val fechaFormateada = mesSeleccionado.value.format(formatter).capitalizar()

    // Cálculos
    val laborables = resumenMensual.filter {
        it.diaSemana.lowercase() !in listOf("sábado", "domingo")
    }
    val findes = resumenMensual.filter {
        it.diaSemana.lowercase() in listOf("sábado", "domingo")
    }
    val horasLaborables = laborables.sumOf { maxOf(0, it.totalMinutos - 240) }
    val horasFindes = findes.sumOf { maxOf(0, it.totalMinutos - 240) }
    val tarifaLaborable = tarifaExtra.doubleValue
    val tarifaFinde = tarifaExtra.doubleValue
    val importeLaboral = (horasLaborables / 60.0) * tarifaLaborable
    val importeFinde = (horasFindes / 60.0) * tarifaFinde
    val totalEuros = importeLaboral + importeFinde

    val scope = rememberCoroutineScope()
    val viewModel: InformeViewModel = hiltViewModel()
    val uiState = viewModel.uiState.collectAsState().value
    val pdfSaver = rememberPdfSaver { uri ->
        val bytes = (uiState as? InformeUiState.Success)?.pdfBytes ?: return@rememberPdfSaver

        scope.launch {
            val ok = viewModel.guardarInforme(uri, context.contentResolver, bytes)

            if (ok){
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    setDataAndType(uri, "application/pdf")
                    flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK
                }
                context.packageManager?.let { pm ->
                    if (intent.resolveActivity(pm) != null) {
                        context.startActivity(intent)
                    }
                }
            }
        }
    }

    val usuarioService = remember {
        RetrofitClient.getService(
            UsuarioService::class.java,
            tokenProvider = { prefs.getString("TOKEN_KEY", token)}
        )
    }

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
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(AzulNoche, MaterialTheme.colorScheme.background)
                    )
                )
        ) {
            EncabezadoInforme(fechaFormateada)

            AccionesInforme(
                onSeleccionarMes = {
                    mostrarFechaPicker(
                        context = context,
                        fechaActual = mesSeleccionado.value
                    ) {
                        mesSeleccionado.value = it.withDayOfMonth(1)
                    }
                },
                onExportar = {
                    val mesParam = mesSeleccionado.value.format(DateTimeFormatter.ofPattern("yyyy-MM"))
                    scope.launch {
                        Log.d("Http", "Token antes de la petición PDF: $token")
                        viewModel.descargarInforme(YearMonth.parse(mesParam, DateTimeFormatter.ofPattern("yyyy-MM")))

                        viewModel.uiState.collect {
                            if (it is InformeUiState.Success){
                                pdfSaver.launch("Informe_$fechaFormateada.pdf")
                                return@collect
                            } else if (it is InformeUiState.Error){
                                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                                return@collect
                            }
                        }
                    }



/*
                    val mesParam = mesSeleccionado.value.format(DateTimeFormatter.ofPattern("yyyy-MM"))
                    val bearer = "Bearer $token"
                    LaunchedEffect(mesParam) {

                        val response = api.descargarPdf(bearer,mesParam)


                        if (response.isSuccessful) {
                            response.body()?.byteStream()?.use { input ->
                                val filename = File(context.cacheDir, "Informe_$mesParam.pdf")
                                FileOutputStream(filename).use { output ->
                                    input.copyTo(output)
                                }
                                openPdf(context, filename)
                            }
                        } else {
                            val msg = response.errorBody()?.string().orEmpty()
                            Toast.makeText(context, "Error descarga: $msg", Toast.LENGTH_SHORT)
                                .show()
                        }

                         */
                        /*
                        val filename = "Informe_$mesParam.pdf"
                        val file = guardarPdfDesdeResponse(
                            response = response,
                            context = context,
                            filename = filename
                        )

                        withContext(Dispatchers.Main) {
                            if (file != null) {
                                Toast.makeText(context, "PDF guardado en: ${file.absolutePath}", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "Error al generar el PDF:", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }*/
                }
            )


            LaunchedEffect(mesSeleccionado.value) {

                val inicio = mesSeleccionado.value.withDayOfMonth(1).toIsoString()
                val fin =
                    mesSeleccionado.value.withDayOfMonth(mesSeleccionado.value.lengthOfMonth()).toIsoString()

                val registrosMesDto = usuarioService.obtenerRegistros(idUsuario, inicio,fin)
                val registrosMes: List<Registro> = registrosMesDto.map { it.toRegistro() }
                val resumenMuestra = registrosDia(registrosMes).toList()

                resumenMensual.clear()
                resumenMensual.addAll(resumenMuestra)
            }

            Box(
                modifier = Modifier
                    .weight(1f)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    item {
                        if (resumenMensual.isNotEmpty()) {
                            TablaInformeMensual(
                                resumenes = resumenMensual
                            )

                            Spacer(modifier = Modifier.height(8.dp))
                        } else {
                            Text(
                                text = "No hay registros para el mes seleccionado",
                                style = MaterialTheme.typography.labelMedium,
                                color = Blanco
                            )
                        }
                    }
                }
            }

            ResumenTotales(
                horasLaborables,
                horasFindes,
                tarifaLaborable,
                tarifaFinde,
                importeLaboral,
                importeFinde,
                totalEuros
            )
        }
    }
}