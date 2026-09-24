package com.example.cineexplorer.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cineexplorer.data.MovieRepository
import com.example.cineexplorer.data.SettingsRepository
import com.example.cineexplorer.data.model.Genre
import com.example.cineexplorer.ui.state.LoadState
import com.example.cineexplorer.ui.state.toUserMessage
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GenresViewModel(
    private val movieRepository: MovieRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    private val _state = MutableStateFlow<LoadState<List<Genre>>>(LoadState.Loading)
    val state: StateFlow<LoadState<List<Genre>>> = _state.asStateFlow()
    private var loadJob: Job? = null

    init {
        load()
    }

    fun load() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _state.value = LoadState.Loading
            try {
                val allGenres = movieRepository.getGenres()
                settingsRepository.settings.collect { settings ->
                    val visible = if (settings.isConfigured) {
                        allGenres.filter { it.id in settings.visibleGenreIds }
                    } else {
                        allGenres
                    }
                    val ordered = visible.sortedWith(
                        compareByDescending<Genre> { it.id == settings.featuredGenreId }
                            .thenBy { it.name }
                    )
                    _state.value = LoadState.Success(ordered)
                }
            } catch (exception: Exception) {
                _state.value = LoadState.Error(exception.toUserMessage())
            }
        }
    }
}
