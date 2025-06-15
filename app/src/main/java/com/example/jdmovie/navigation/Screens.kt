package com.example.jdmovie.navigation

sealed class Screens(val route: String) {
    object HomeRoute : Screens("Home")
    object MovieDetailRoute : Screens("MovieDetail")
//    object MoviePlayerRoute : Screens("MoviePlayer")
    object AppRoute : Screens("App")
}