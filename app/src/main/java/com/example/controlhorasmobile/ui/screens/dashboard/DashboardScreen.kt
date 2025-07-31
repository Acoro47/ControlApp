package com.example.controlhorasmobile.ui.screens.dashboard

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.controlhorasmobile.model.Registro
import com.example.controlhorasmobile.model.RegistroItem
import com.example.controlhorasmobile.model.dto.RegistroDTO
import com.example.controlhorasmobile.model.dto.toRegistro
import com.example.controlhorasmobile.network.RetrofitClient
import com.example.controlhorasmobile.network.UsuarioService
import com.example.controlhorasmobile.ui.screens.dashboard.components.BotonFichaje
import com.example.controlhorasmobile.ui.screens.dashboard.components.CardResumen
import com.example.controlhorasmobile.ui.screens.dashboard.logic.DashboardCalculations
import com.example.controlhorasmobile.ui.screens.dashboard.logic.DashboardCalculations.toIsoString
import com.example.controlhorasmobile.ui.theme.AzulNoche
import com.example.controlhorasmobile.ui.theme.ColorSecundario
import com.example.controlhorasmobile.utils.cerrarSesion
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale



@Composable
fun DashboardScreen(navController: NavController) {

    val context = LocalContext.current
    val prefs = context.getSharedPreferences("usuario", Context.MODE_PRIVATE)
    val token = prefs.getString("token", "").orEmpty()


    val idUsuario = prefs.getLong("idUsuario", -1L)
    val username = prefs.getString("username", "Usuario") ?: "Usuario"

    val registrosMes = remember { mutableStateListOf<Registro>() }
    val registrosHoy = remember { mutableStateListOf<Registro>() }

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val formato = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val fechaFormato = formato.format(Date())

    val alturaCabecera by remember { mutableIntStateOf(0) }

    val diasTrabajados = remember { derivedStateOf { DashboardCalculations.calcularDiasTrabajados(registrosMes) }}

    val horasMes = remember { derivedStateOf { DashboardCalculations.calcularHorasMes(registrosMes) } }
    val diaLibre = remember { derivedStateOf { DashboardCalculations.obtenerProximoMiercoles() } }

    val usuarioService = remember {
        RetrofitClient.getService(
            UsuarioService::class.java,
            tokenProvider = { prefs.getString("token", "")}
            )
    }



    val isLoading = remember { mutableStateOf(false) }
    /**
     * funcion para recargar los registros desde el backend
     * sin parámetros
     */

    fun recargaRegistros(){
        scope.launch {
            isLoading.value = true
            try {

                val hoy = LocalDate.now()

                val desde = hoy.toIsoString()
                val hasta = hoy.toIsoString()

                val nuevosDto: List<RegistroDTO> = usuarioService.obtenerRegistros(
                    idUsuario,
                    desde,
                    hasta
                )

                val nuevos:List<Registro> = nuevosDto.map { it.toRegistro() }
                registrosHoy.clear()
                registrosHoy.addAll(nuevos)

            } catch (e: retrofit2.HttpException) {
                if (e.code() == 401){
                    navController.navigate("login")
                }
                else {
                    Log.e("Dashboard","Error HTTP ${e.code()}")
                }

            } catch (e: Exception) {
                Log.e("Dashboard", "Error red u otro: ${e.message}")
            }
            finally {
                isLoading.value = false
            }

        }
    }

    /**
     * Función para registrar las horas de entrada
     */
    fun registroEntrada() {
        val hayEntradaSinSalida = registrosHoy.any {
            it.horaEntrada != "-" && it.horaSalida == "-"
        }

        if (hayEntradaSinSalida){
            Log.e("Entrada", "Ya existe una entrada sin salida. Se bloquea registro duplicado")
            scope.launch {
                snackbarHostState.showSnackbar("Ya tienes una entrada activa")
            }
        }

        Log.d("Entrada", "✅ Registrando entrada")

        scope.launch {
            try {
                val response = usuarioService.registrarEntrada(idUsuario)
                Log.e("Dashboard", "Llamando a la función de registrar entrada")
                if (response.isSuccessful) {
                    Log.d("Entrada", "✅ Entrada registrada")
                    recargaRegistros()
                } else {
                    Log.e("Entrada", "❌ Error: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("Entrada", "⚠\uFE0F Excepción: ${e.message}")
            }
        }
    }

    /**
     * Función para registrar las horas de salida
     */

    fun registroSalida() {
        scope.launch {
            try {
                val response = usuarioService.registrarSalida(idUsuario)
                if (response.isSuccessful) {
                    Log.d("Salida", "✅ Salida registrada")
                    recargaRegistros()
                } else {
                    Log.e("Salida", "❌ Error: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("Salida", "⚠\uFE0F Excepción: ${e.message}")
            }
        }
    }

    // Lógica para activar los botones
    val ultimoRegistro = registrosHoy.lastOrNull()
    val puedeRegistrarEntrada = ultimoRegistro == null ||
            ultimoRegistro.horaSalida != "-"
    val puedeRegistrarSalida = ultimoRegistro != null &&
            ultimoRegistro.horaEntrada != "-" &&
            ultimoRegistro.horaSalida == "-"

    // Cargar los datos al abrir la pantalla
    LaunchedEffect(Unit) {
        isLoading.value = true
        try {
            if (idUsuario != -1L) {
                val (inicio, final) = DashboardCalculations.obtenerRangoMesActual()

                val desde = inicio.toIsoString()
                val hasta = final.toIsoString()

                val registrosDiaDto: List<RegistroDTO> = usuarioService.obtenerRegistros(
                    idUsuario,
                    desde,
                    hasta
                )
                val registrosDia: List<Registro> = registrosDiaDto.map { it.toRegistro() }
                registrosHoy.clear()
                registrosHoy.addAll(registrosDia)

                /*
                val registrosMesActualDto: List<RegistroDTO> = usuarioService.obtenerRegistros(
                    idUsuario,
                    inicio,
                    final
                )
                val registros: List<Registro> = registrosMesActualDto.map { it.toRegistro() }
                registrosMes.clear()
                registrosMes.addAll(registros)

                 */
            }
        } catch (e: retrofit2.HttpException){
            if (e.code() == 401){
                navController.navigate("login")
            } else {
                Log.e("Dashboard", "Error HTTP ${e.code()}")
            }
        }
        catch (e: Exception) {
            Log.e("Dashboard","Error de red u otro: ${e.message}")
        }

        finally {
            isLoading.value = false
        }

    }

    // Interfaz principal con Scaffold para el SnackBar
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        floatingActionButton = {
            if (!isLoading.value && registrosHoy.isNotEmpty()) {
                FloatingActionButton(onClick = { recargaRegistros() }) {
                    Icon(Icons.Default.Refresh, contentDescription = "Recargar")
                }
            }
    },
        bottomBar = {
            Surface(
                shadowElevation = 4.dp,
                color = MaterialTheme.colorScheme.background

            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 16.dp,
                            vertical = 12.dp
                        )
                        .navigationBarsPadding(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    BotonFichaje(
                        texto = "Entrada",
                        onClick = { registroEntrada() },
                        enabled = puedeRegistrarEntrada,
                        modifier = Modifier
                            .weight(1f)
                    )
                    BotonFichaje(
                        texto = "Salida",
                        onClick = { registroSalida() },
                        enabled = puedeRegistrarSalida,
                        modifier = Modifier
                            .weight(1f)
                    )

                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(AzulNoche, MaterialTheme.colorScheme.background),
                        startY = alturaCabecera.toFloat(),
                        endY = alturaCabecera.toFloat() + 600f
                    )
                )
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
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

                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                HorizontalDivider(
                    thickness = 5.dp,
                    color = ColorSecundario)
                Spacer(modifier = Modifier.height(16.dp))

                CardResumen(
                    diasTrabajados = diasTrabajados.value,
                    horasMes = horasMes.value,
                    diaLibre = diaLibre.value
                )

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(
                    thickness = 5.dp,
                    color = ColorSecundario)
                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ){
                    if (isLoading.value){
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else {

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        ){
                            if (registrosHoy.isEmpty()) {
                                Text(
                                    text = "Hola ${username.uppercase()}, aún no has registrado ninguna entrada",
                                    style = MaterialTheme.typography.titleSmall
                                )
                                Spacer(modifier = Modifier.height(18.dp))
                                Text(
                                    text ="Para registrar el inicio pulsa Entrada",
                                    style = MaterialTheme.typography.titleMedium
                                )
                            } else {

                                LazyColumn {
                                    items(registrosHoy) { registro ->
                                        Log.e("Dashboard", "Este es cada registro: $registro")
                                        RegistroItem(registro)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}