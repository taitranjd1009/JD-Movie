package com.example.jdmovie.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.jdmovie.components.MovieList
import com.example.jdmovie.components.SearchMovie
import com.example.jdmovie.models.Film

sealed interface HomeScreenViewState {
    object Loading : HomeScreenViewState
    data class GridDisplay(
        val movies: List<Film> = emptyList()
    ) : HomeScreenViewState
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    Column() {
        SearchMovie(navController)
        MovieList(navController)
    }

}

