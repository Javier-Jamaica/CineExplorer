package com.example.cineexplorer.data.remote

import com.example.cineexplorer.data.model.CreditsResponse
import com.example.cineexplorer.data.model.GenreResponse
import com.example.cineexplorer.data.model.MovieDetailsDto
import com.example.cineexplorer.data.model.MoviePage
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApi {
    @GET("genre/movie/list")
    suspend fun getGenres(
        @Query("language") language: String = "es-ES"
    ): GenreResponse

    @GET("discover/movie")
    suspend fun discoverMovies(
        @Query("with_genres") genreId: Int,
        @Query("page") page: Int,
        @Query("language") language: String = "es-ES",
        @Query("sort_by") sortBy: String = "popularity.desc",
        @Query("include_adult") includeAdult: Boolean = false
    ): MoviePage

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("language") language: String = "es-ES"
    ): MovieDetailsDto

    @GET("movie/{movie_id}/credits")
    suspend fun getMovieCredits(
        @Path("movie_id") movieId: Int,
        @Query("language") language: String = "es-ES"
    ): CreditsResponse
}
