package com.example.controlhorasmobile.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.controlhorasmobile.session.Session
import com.example.controlhorasmobile.ui.screens.dashboard.DashboardScreen
import com.example.controlhorasmobile.ui.screens.informes.InformePreviewScreen
import com.example.controlhorasmobile.ui.screens.informes.InformeScreen
import com.example.controlhorasmobile.ui.screens.login.LoginScreen
import com.example.controlhorasmobile.ui.screens.solicitud.SolicitarTokenScreen

@Composable
fun AppNavigation(navController: NavHostController){
    NavHost(navController = navController, startDestination = "login") {
        composable("login") { LoginScreen(navController) }
        composable("solicitarToken") { SolicitarTokenScreen(navController) }
        composable("dashboard") { DashboardScreen(navController) }
        composable("informes") {
            val context = LocalContext.current
            val idUsuario = remember { Session.obtenerIdUsuarioDesdePrefs(context) }
            InformeScreen(
                idUsuario = idUsuario,
                navController = navController
            )
        }
        composable("informeMensual") {
            val context = LocalContext.current
            val idUsuario = remember { Session.obtenerIdUsuarioDesdePrefs(context) }
            InformePreviewScreen(
                idUsuario = idUsuario,
                context = context,
                navController = navController
            )
        }

    }
}