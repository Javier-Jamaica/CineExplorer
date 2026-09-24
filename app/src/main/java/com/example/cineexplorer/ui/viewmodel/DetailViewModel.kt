package com.example.cineexplorer.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cineexplorer.data.MovieRepository
import com.example.cineexplorer.data.model.MovieDetail
import com.example.cineexplorer.ui.state.LoadState
import com.example.cineexplorer.ui.state.toUserMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailViewModel(
    private val repository: MovieRepository,
    private val movieId: Int
) : ViewModel() {
    private val _state = MutableStateFlow<LoadState<MovieDetail>>(LoadState.Loading)
    val state: StateFlow<LoadState<MovieDetail>> = _state.asStateFlow()

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            _state.value = LoadState.Loading
            try {
                _state.value = LoadState.Success(repository.getMovieDetail(movieId))
            } catch (exception: Exception) {
                _state.value = LoadState.Error(exception.toUserMessage())
            }
        }
    }
}
