package com.example.cineexplorer.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cineexplorer.data.MovieRepository
import com.example.cineexplorer.data.SettingsRepository
import com.example.cineexplorer.data.model.Genre
import com.example.cineexplorer.ui.state.toUserMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class AdminUiState(
    val isLoading: Boolean = true,
    val genres: List<Genre> = emptyList(),
    val selectedIds: Set<Int> = emptySet(),
    val featuredId: Int? = null,
    val error: String? = null,
    val confirmation: String? = null
)

class AdminViewModel(
    private val movieRepository: MovieRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    private val _state = MutableStateFlow(AdminUiState())
    val state: StateFlow<AdminUiState> = _state.asStateFlow()

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            _state.value = AdminUiState(isLoading = true)
            try {
                val genres = movieRepository.getGenres()
                val settings = settingsRepository.settings.first()
                val selected = if (settings.isConfigured) {
                    settings.visibleGenreIds.intersect(genres.map { it.id }.toSet())
                } else {
                    genres.map { it.id }.toSet()
                }
                _state.value = AdminUiState(
                    isLoading = false,
                    genres = genres,
                    selectedIds = selected.ifEmpty { genres.map { it.id }.toSet() },
                    featuredId = settings.featuredGenreId?.takeIf { it in selected }
                )
            } catch (exception: Exception) {
                _state.value = AdminUiState(
                    isLoading = false,
                    error = exception.toUserMessage()
                )
            }
        }
    }

    fun toggleGenre(genreId: Int) {
        val current = _state.value
        val next = current.selectedIds.toMutableSet()
        if (genreId in next) {
            if (next.size == 1) return
            next.remove(genreId)
        } else {
            next.add(genreId)
        }
        _state.value = current.copy(
            selectedIds = next,
            featuredId = current.featuredId?.takeIf { it in next },
            confirmation = null
        )
    }

    fun selectFeatured(genreId: Int) {
        if (genreId !in _state.value.selectedIds) return
        _state.value = _state.value.copy(featuredId = genreId, confirmation = null)
    }

    fun save() {
        val current = _state.value
        viewModelScope.launch {
            settingsRepository.save(current.selectedIds, current.featuredId)
            _state.value = _state.value.copy(confirmation = "Configuración guardada.")
        }
    }

    fun reset() {
        viewModelScope.launch {
            settingsRepository.reset()
            _state.value = _state.value.copy(
                selectedIds = _state.value.genres.map { it.id }.toSet(),
                featuredId = null,
                confirmation = "Se restauraron todos los géneros."
            )
        }
    }
}
