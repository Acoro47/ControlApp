package com.example.controlhorasmobile.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
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
        composable("informes") { InformeScreen(navController) }
        composable("informeMensual") { InformePreviewScreen(navController) }

    }
}