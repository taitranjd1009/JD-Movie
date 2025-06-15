package com.example.jdmovie.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.jdmovie.ui.screens.HomeScreenViewState
import com.example.jdmovie.viewmodels.HomeScreenViewModel


@Composable
fun MovieList(
    navController: NavController,
    viewModel: HomeScreenViewModel = hiltViewModel(),
) {
    val viewState by viewModel.viewState.collectAsState()

    LaunchedEffect(key1 = viewModel, block = { viewModel.fetchInitialPage() })

    when (val state = viewState) {
        HomeScreenViewState.Loading -> CircularProgressIndicator(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 128.dp),
//            color = RickAction
        )

        is HomeScreenViewState.GridDisplay -> {
            LazyVerticalGrid(
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                columns = GridCells.Fixed(3),
                content = {
                    items(items = state.movies, key = { it._id }) { item ->
                        MovieCard(navController = navController, film = item)
                    }
                }

            )
        }
    }
}