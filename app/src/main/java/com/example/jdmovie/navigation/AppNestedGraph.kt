package com.example.jdmovie.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.jdmovie.models.Film
import com.example.jdmovie.ui.screens.HomeScreen
import com.example.jdmovie.ui.screens.MovieDetailScreen
import com.google.gson.Gson
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

fun NavGraphBuilder.appGraph(navController: NavController) {
    navigation(startDestination = Screens.HomeRoute.route, route = Screens.AppRoute.route) {
        composable(Screens.HomeRoute.route) {
            HomeScreen(navController = navController)
        }
        composable(Screens.MovieDetailRoute.route + "/{movie}") {
            val encodedMovie = it.arguments?.getString("movie")
            val decodedMovieJson = encodedMovie?.let {
                URLDecoder.decode(it, StandardCharsets.UTF_8.toString())
            }
            val film = Gson().fromJson(decodedMovieJson, Film::class.java)
            MovieDetailScreen(navController = navController, film)
        }

//        composable(Screens.MoviePlayerRoute.route + "/{movieUrl}") {
//            val movieUrl = it.arguments?.getString("movieUrl")
//            val movieUrlString = java.net.URLDecoder.decode(movieUrl, StandardCharsets.UTF_8.toString())
//            MoviePlayerScreen(movieUrlString ?: "")
//        }
    }
}