package com.example.controlhorasmobile.ui.screens.solicitud

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.controlhorasmobile.network.RetrofitClient
import com.example.controlhorasmobile.network.UsuarioService
import kotlinx.coroutines.launch


@Composable
fun SolicitarTokenScreen(navController: NavController){
    var email by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf<String?>(null) }
    var scope = rememberCoroutineScope()



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "\uD83D\uDCE9 Solicita tu token",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = username,
            onValueChange = {username = it},
            label = {Text("Nombre de usuario")},
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = {email = it},
            label = {Text("Correo electrónico")},
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                scope.launch {
                    try {
                        val usuarioService = RetrofitClient.getService(UsuarioService::class.java)
                        val response = usuarioService.solicitarToken(username, email)
                        if (response.isSuccessful) {
                            mensaje = response.body()?.mensaje ?: "✅ Token enviado. Revisa tu correo."
                        }
                        else {
                            mensaje = "❌ No se pudo enviar el token."
                        }
                    }
                    catch (e: Exception){
                        mensaje = "⚠\uFE0F Error de red: ${e.message}"
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Solicitar token")
        }

        Spacer(modifier = Modifier.height(16.dp))

        mensaje?.let {
            Text(
                text = it,
                color = when {
                    it.startsWith("✅") -> Color.Green
                    it.startsWith("❌") -> Color.Red
                    else -> Color.Gray
                }
            )
        }
        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver")
        }
    }

}