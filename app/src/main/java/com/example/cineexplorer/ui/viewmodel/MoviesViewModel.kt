package com.example.cineexplorer.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cineexplorer.data.MovieRepository
import com.example.cineexplorer.data.model.MovieSummary
import com.example.cineexplorer.ui.state.toUserMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class MoviesUiState(
    val isLoading: Boolean = true,
    val isLoadingMore: Boolean = false,
    val movies: List<MovieSummary> = emptyList(),
    val currentPage: Int = 0,
    val totalPages: Int = 1,
    val error: String? = null
) {
    val canLoadMore: Boolean get() = currentPage in 1 until totalPages && !isLoadingMore
}

class MoviesViewModel(
    private val repository: MovieRepository,
    private val genreId: Int
) : ViewModel() {
    private val _state = MutableStateFlow(MoviesUiState())
    val state: StateFlow<MoviesUiState> = _state.asStateFlow()

    init {
        loadFirstPage()
    }

    fun loadFirstPage() {
        viewModelScope.launch {
            _state.value = MoviesUiState(isLoading = true)
            try {
                val result = repository.getMovies(genreId = genreId, page = 1)
                _state.value = MoviesUiState(
                    isLoading = false,
                    movies = result.results,
                    currentPage = result.page,
                    totalPages = result.totalPages
                )
            } catch (exception: Exception) {
                _state.value = MoviesUiState(
                    isLoading = false,
                    error = exception.toUserMessage()
                )
            }
        }
    }

    fun loadMore() {
        val snapshot = _state.value
        if (!snapshot.canLoadMore) return

        viewModelScope.launch {
            _state.value = snapshot.copy(isLoadingMore = true, error = null)
            try {
                val result = repository.getMovies(genreId, snapshot.currentPage + 1)
                _state.value = snapshot.copy(
                    isLoadingMore = false,
                    movies = (snapshot.movies + result.results).distinctBy { it.id },
                    currentPage = result.page,
                    totalPages = result.totalPages
                )
            } catch (exception: Exception) {
                _state.value = snapshot.copy(
                    isLoadingMore = false,
                    error = exception.toUserMessage()
                )
            }
        }
    }
}
