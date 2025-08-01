package com.example.controlhorasmobile.ui.screens.login

import android.content.Context
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import com.example.controlhorasmobile.R
import com.example.controlhorasmobile.network.AuthService
import com.example.controlhorasmobile.network.LoginRequest
import com.example.controlhorasmobile.network.RetrofitClient
import com.example.controlhorasmobile.ui.theme.AzulNoche
import com.example.controlhorasmobile.ui.theme.AzulProfundo
import com.example.controlhorasmobile.ui.theme.Blanco
import com.example.controlhorasmobile.ui.theme.ColorPrimario
import com.example.controlhorasmobile.ui.theme.VerdeBrisa
import com.example.controlhorasmobile.ui.theme.customTextFieldColors
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController) {

    val context = LocalContext.current
    val prefs = context.getSharedPreferences("usuario", Context.MODE_PRIVATE)
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val isPasswordVisible = remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    var mensaje by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()
    var logoVisible by remember { mutableStateOf(false) }
    val authService = RetrofitClient.getService(AuthService::class.java)

    LaunchedEffect(Unit) {
        logoVisible = true
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier.fillMaxWidth()
    ) {
        padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(AzulProfundo, MaterialTheme.colorScheme.background),
                        startY = 0f,
                        endY = 900f
                    )
                )
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(padding)
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AnimatedVisibility(
                    visible = logoVisible,
                    enter = fadeIn()
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.control_horas),
                        contentDescription = "Logo Control Horas",
                        modifier = Modifier
                            .size(120.dp)
                            .padding(bottom = 16.dp)
                    )
                }

                Text(
                    text = "Bienvenido a Control Horas",
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = MaterialTheme.colorScheme.primary
                    )
                )

                Spacer(modifier = Modifier.height(32.dp))

                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Usuario") },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = null
                        )
                    },
                    colors = customTextFieldColors(),
                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                    trailingIcon = {
                        val icon =
                            if (isPasswordVisible.value) Icons.Default.Visibility else Icons.Default.VisibilityOff
                        IconButton(
                            onClick = {
                                isPasswordVisible.value = !isPasswordVisible.value
                            }) {
                            Icon(
                                imageVector = icon,
                                contentDescription = "Mostrar contraseña"
                            )
                        }
                    },
                    visualTransformation = if (isPasswordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
                    colors = customTextFieldColors(),
                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        scope.launch {
                            try {

                                val response = authService.loginUsuario(
                                    LoginRequest(username,password)
                                )
                                Log.d(
                                    "Login",
                                    "Código: ${response.code()}, cuerpo: ${response.body()}"
                                )
                                if (response.isSuccessful) {
                                    Log.d("Login", "Respuesta recibida ${response.body()}")
                                    val usuario = response.body()!!
                                    prefs.edit()
                                        .putString("TOKEN_KEY", usuario.token)
                                        .putString("name", usuario.username)
                                        .putLong("idUsuario", usuario.id)
                                        .apply()


                                    mensaje = "✅ Inicio correcto"
                                    navController.navigate("dashboard") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                } else {
                                    val errorBody = response.errorBody()?.string()
                                    Log.e("Login", "Error HTTP ${response.code()}: $errorBody")
                                    mensaje = "❌ Usuario o contraseña incorrectos"
                                }
                            } catch (e: Exception) {
                                mensaje = "⚠\uFE0F Error de red: ${e.message}"
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(48.dp)
                        .border(
                            1.dp,
                            VerdeBrisa,
                            shape = MaterialTheme.shapes.medium
                        ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Blanco,
                        contentColor = VerdeBrisa
                    )
                ) {
                    Text(
                        text = "Iniciar Sesion",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Medium
                        )
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                TextButton(
                    onClick = { navController.navigate("solicitarToken") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "¿No tienes cuenta? Solicita acceso",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = AzulNoche,
                            textDecoration = TextDecoration.Underline
                        )
                    )

                }

                Spacer(modifier = Modifier.height(12.dp))

                mensaje?.let {
                    Text(
                        text = it,
                        color = ColorPrimario
                    )
                }
            }
        }
    }
}