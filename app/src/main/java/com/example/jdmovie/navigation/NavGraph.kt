package com.example.jdmovie.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.jdmovie.navigation.Screens
import com.example.jdmovie.navigation.appGraph

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = Screens.AppRoute.route) {
        appGraph(navController)
    }
}