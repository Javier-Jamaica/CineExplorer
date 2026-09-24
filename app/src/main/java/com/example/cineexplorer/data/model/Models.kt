package com.example.cineexplorer.data.model

import com.google.gson.annotations.SerializedName

data class GenreResponse(
    val genres: List<Genre> = emptyList()
)

data class Genre(
    val id: Int,
    val name: String
)

data class MoviePage(
    val page: Int = 1,
    val results: List<MovieSummary> = emptyList(),
    @SerializedName("total_pages") val totalPages: Int = 1
)

data class MovieSummary(
    val id: Int,
    val title: String,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("release_date") val releaseDate: String = "",
    @SerializedName("vote_average") val voteAverage: Double = 0.0
) {
    val year: String get() = releaseDate.take(4).ifBlank { "Sin fecha" }
    val posterUrl: String? get() = posterPath?.let { "$IMAGE_BASE_URL$it" }
}

data class MovieDetailsDto(
    val id: Int,
    val title: String,
    val overview: String = "",
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("release_date") val releaseDate: String = "",
    val runtime: Int?,
    @SerializedName("vote_average") val voteAverage: Double = 0.0,
    val genres: List<Genre> = emptyList()
)

data class CreditsResponse(
    val cast: List<CastMember> = emptyList(),
    val crew: List<CrewMember> = emptyList()
)

data class CastMember(
    val name: String,
    val character: String = "",
    val order: Int = Int.MAX_VALUE
)

data class CrewMember(
    val name: String,
    val job: String
)

data class MovieDetail(
    val id: Int,
    val title: String,
    val year: String,
    val overview: String,
    val posterUrl: String?,
    val runtimeLabel: String,
    val ratingLabel: String,
    val genresLabel: String,
    val director: String,
    val castLabel: String
)

fun MovieDetailsDto.toDomain(credits: CreditsResponse): MovieDetail {
    val director = credits.crew.firstOrNull { it.job.equals("Director", ignoreCase = true) }?.name
        ?: "No disponible"
    val cast = credits.cast
        .sortedBy { it.order }
        .take(5)
        .joinToString { member ->
            if (member.character.isBlank()) member.name else "${member.name} (${member.character})"
        }
        .ifBlank { "No disponible" }

    return MovieDetail(
        id = id,
        title = title,
        year = releaseDate.take(4).ifBlank { "Sin fecha" },
        overview = overview.ifBlank { "TMDB no ofrece una sinopsis en español para esta película." },
        posterUrl = posterPath?.let { "$IMAGE_BASE_URL$it" },
        runtimeLabel = runtime?.takeIf { it > 0 }?.let { "$it min" } ?: "No disponible",
        ratingLabel = String.format("%.1f / 10", voteAverage),
        genresLabel = genres.joinToString { it.name }.ifBlank { "No disponible" },
        director = director,
        castLabel = cast
    )
}

private const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"
