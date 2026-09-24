package com.example.cineexplorer.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "cineexplorer_settings")

data class GenreSettings(
    val isConfigured: Boolean = false,
    val visibleGenreIds: Set<Int> = emptySet(),
    val featuredGenreId: Int? = null
)

class SettingsRepository(private val context: Context) {
    val settings: Flow<GenreSettings> = context.dataStore.data.map { preferences ->
        GenreSettings(
            isConfigured = preferences[Keys.CONFIGURED] ?: false,
            visibleGenreIds = preferences[Keys.VISIBLE_GENRES]
                .orEmpty()
                .mapNotNull(String::toIntOrNull)
                .toSet(),
            featuredGenreId = preferences[Keys.FEATURED_GENRE]
        )
    }

    suspend fun save(visibleGenreIds: Set<Int>, featuredGenreId: Int?) {
        context.dataStore.edit { preferences ->
            preferences[Keys.CONFIGURED] = true
            preferences[Keys.VISIBLE_GENRES] = visibleGenreIds.map(Int::toString).toSet()
            if (featuredGenreId == null) {
                preferences.remove(Keys.FEATURED_GENRE)
            } else {
                preferences[Keys.FEATURED_GENRE] = featuredGenreId
            }
        }
    }

    suspend fun reset() {
        context.dataStore.edit { preferences ->
            preferences.remove(Keys.CONFIGURED)
            preferences.remove(Keys.VISIBLE_GENRES)
            preferences.remove(Keys.FEATURED_GENRE)
        }
    }

    private object Keys {
        val CONFIGURED = booleanPreferencesKey("genres_configured")
        val VISIBLE_GENRES = stringSetPreferencesKey("visible_genre_ids")
        val FEATURED_GENRE = intPreferencesKey("featured_genre_id")
    }
}
