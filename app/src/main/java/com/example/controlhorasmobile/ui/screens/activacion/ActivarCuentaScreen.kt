package com.example.controlhorasmobile.ui.screens.activacion


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Button
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.unit.dp
import com.example.controlhorasmobile.model.DatosActivacion
import com.example.controlhorasmobile.network.AuthService
import com.example.controlhorasmobile.network.RetrofitClient

@Composable
fun ActivarCuentaScreen(
    onActivar: (String) -> Unit,
    mensaje: String?
) {
    var token by remember { mutableStateOf("") }
    var activarToken by remember { mutableStateOf<String?>(null) }
    var mensajeError by remember { mutableStateOf<String?>(null) }
    var datos by remember { mutableStateOf<DatosActivacion?>(null) }

    if (activarToken != null){
        LaunchedEffect(activarToken) {
            try {
                val authService = RetrofitClient.getService(AuthService::class.java)
                /*
                val response = authService.activarCuenta(activarToken!!)
                if (response.isSuccessful){
                    datos = response.body()
                    mensajeError = null
                }
                else {
                    mensajeError = "❌ Token inválido o caducado"
                    datos = null
                }

                 */
            }
            catch (e: Exception){
                mensajeError = "⚠\uFE0F Error de conexión: ${e.message}"
                datos = null
            }
            activarToken = null
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement =  Arrangement.Center
    ){
        Text(
            text = "\uD83D\uDD13 Activar cuenta",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = token,
            onValueChange = { token = it },
            label = { Text("Token de activación") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onActivar(token) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Activar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mensaje de error si falla la activación
        mensaje?.let {
            Text(text = it, color= Color.Red)
        }

        // Mostrar datos si el token fue válido

        datos?.let {
            Text(text =" Bienvenido ${it.username} (${it.email})", color = Color.Green)
        }
    }
}