package com.example.jdmovie.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jdmovie.models.FilmResponse
import com.example.jdmovie.repositories.MovieRepository
import com.example.jdmovie.ui.screens.HomeScreenViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {
    private val _viewState = MutableStateFlow<HomeScreenViewState>(HomeScreenViewState.Loading)
    val viewState: StateFlow<HomeScreenViewState> = _viewState.asStateFlow()

    private val filmReponsive = mutableListOf<FilmResponse>()

    fun fetchInitialPage() = viewModelScope.launch {
        if (filmReponsive.isNotEmpty()) return@launch
        val initialPage = movieRepository.fetchMovies()
        initialPage.onSuccess { data ->
//            fetchedCharacterPages.clear()
//            fetchedCharacterPages.add(characterPage)
            _viewState.update {
                return@update HomeScreenViewState.GridDisplay(movies = data.items)
            }
        }.onFailure {
            // todo
        }
    }

}