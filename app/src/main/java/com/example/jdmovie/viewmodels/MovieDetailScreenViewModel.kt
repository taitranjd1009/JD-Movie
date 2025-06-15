package com.example.jdmovie.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jdmovie.repositories.MovieRepository
import com.example.jdmovie.ui.screens.MovieDetailViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailScreenViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {
    private val _internalStorageFlow = MutableStateFlow<MovieDetailViewState>(
        value = MovieDetailViewState.Loading
    )
    val stateFlow = _internalStorageFlow.asStateFlow()

    fun fetchMovieDetail(slug: String) = viewModelScope.launch {
        _internalStorageFlow.update { return@update MovieDetailViewState.Loading }
        movieRepository.fetchMovieDetail(slug).onSuccess { responsive ->
            _internalStorageFlow.update {
                return@update MovieDetailViewState.Success(film = responsive.movie, episodes = responsive.episodes)
            }
        }.onFailure { exception ->
            _internalStorageFlow.update {
                return@update MovieDetailViewState.Error(
                    message = exception.message ?: "Unknown error occurred"
                )
            }
        }
    }
}