package com.example.jdmovie.navigation;

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.jdmovie.components.ServerTab
import com.example.jdmovie.models.Episode
import com.example.jdmovie.viewmodels.MovieDetailScreenViewModel

@Composable
fun MovieDetailNavGraph(
    navController: NavHostController,
    startDestination: String,
    episodes: List<Episode>,
    viewModel: MovieDetailScreenViewModel
) {
    NavHost(navController, startDestination = startDestination) {
        episodes.forEachIndexed { index, item ->
            composable(index.toString()) {
                ServerTab(navController = navController, episode = item, viewModel)
            }
        }
    }
}
