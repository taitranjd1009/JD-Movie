package com.example.jdmovie.viewmodels

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jdmovie.models.Film
import com.example.jdmovie.models.FilmResponse
import com.example.jdmovie.repositories.MovieRepository
import com.example.jdmovie.ui.screens.HomeScreenViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchScreenViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {
    val searchTextFieldState = TextFieldState()

    sealed interface SearchState {
        object Empty : SearchState
        data class UserQuery(val query: String) : SearchState
    }

    sealed interface ScreenState {
        object Empty : ScreenState
        object Searching : ScreenState
        data class Error(val message: String) : ScreenState
        data class Content(
            val userQuery: String,
            val results: List<Film>,
            val filterState: FilterState
        ) : ScreenState {
            data class FilterState(
                val totalItems: Int,
                val totalItemsPerPage: Int,
                val currentPage: Int,
                val totalPages: Int
            )
        }
    }

    private val _uiState = MutableStateFlow<ScreenState>(ScreenState.Empty)
    val uiState = _uiState.asStateFlow()

    private val searchTextState: StateFlow<SearchState> = snapshotFlow { searchTextFieldState.text }
        .debounce(500)
        .mapLatest { if (it.isBlank()) SearchState.Empty else SearchState.UserQuery(it.toString()) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 2000),
            initialValue = SearchState.Empty
        )

    fun observeMovieSearch() = viewModelScope.launch {
        searchTextState.collectLatest { searchState ->
            when (searchState) {
                is SearchState.Empty -> _uiState.update { ScreenState.Empty }
                is SearchState.UserQuery -> searchMovies(searchState.query)
            }
        }
    }


    private fun searchMovies(query: String) = viewModelScope.launch {
        _uiState.update { ScreenState.Searching }
        movieRepository.searchMovies(keyword = query).onSuccess { movieRes ->
//            val allStatuses =
//                movies.map { it.status }.toSet().toList().sortedBy { it.displayName }
            _uiState.update {
                ScreenState.Content(
                    userQuery = query,
                    results = movieRes.data.items,
                    filterState = ScreenState.Content.FilterState(
                        totalItems = 0,
                        totalItemsPerPage = 0,
                        currentPage = 0,
                        totalPages = 0
                    )
                )
            }
        }.onFailure { exception ->
            _uiState.update { ScreenState.Error("No search results found") }
        }
    }

}