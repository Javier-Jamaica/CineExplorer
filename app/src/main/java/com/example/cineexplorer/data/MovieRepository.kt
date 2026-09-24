package com.example.cineexplorer.data

import com.example.cineexplorer.data.model.Genre
import com.example.cineexplorer.data.model.MovieDetail
import com.example.cineexplorer.data.model.MoviePage
import com.example.cineexplorer.data.model.toDomain
import com.example.cineexplorer.data.remote.TmdbApi
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class MovieRepository(private val api: TmdbApi) {
    suspend fun getGenres(): List<Genre> = api.getGenres().genres.sortedBy { it.name }

    suspend fun getMovies(genreId: Int, page: Int): MoviePage =
        api.discoverMovies(genreId = genreId, page = page)

    suspend fun getMovieDetail(movieId: Int): MovieDetail = coroutineScope {
        val details = async { api.getMovieDetails(movieId) }
        val credits = async { api.getMovieCredits(movieId) }
        details.await().toDomain(credits.await())
    }
}
