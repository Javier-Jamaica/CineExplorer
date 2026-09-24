package com.example.cineexplorer.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cineexplorer.data.MovieRepository
import com.example.cineexplorer.data.SettingsRepository
import com.example.cineexplorer.data.remote.NetworkModule

class AppContainer(context: Context) {
    val movieRepository = MovieRepository(NetworkModule.api)
    val settingsRepository = SettingsRepository(context)
}

class SimpleViewModelFactory<VM : ViewModel>(
    private val creator: () -> VM
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = creator() as T
}
