package com.example.cineexplorer.data.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ModelsTest {
    @Test
    fun `toDomain obtiene director y limita reparto a cinco personas`() {
        val details = MovieDetailsDto(
            id = 10,
            title = "Película de prueba",
            overview = "Sinopsis",
            posterPath = "/poster.jpg",
            releaseDate = "2026-09-20",
            runtime = 110,
            voteAverage = 8.25,
            genres = listOf(Genre(18, "Drama"))
        )
        val credits = CreditsResponse(
            cast = (1..7).map { CastMember("Actor $it", order = it) },
            crew = listOf(CrewMember("Directora Ejemplo", "Director"))
        )

        val result = details.toDomain(credits)

        assertEquals("Directora Ejemplo", result.director)
        assertEquals("2026", result.year)
        assertEquals("Drama", result.genresLabel)
        assertTrue(result.castLabel.contains("Actor 1"))
        assertTrue(!result.castLabel.contains("Actor 6"))
    }

    @Test
    fun `toDomain usa textos alternativos cuando faltan datos`() {
        val result = MovieDetailsDto(
            id = 11,
            title = "Sin datos",
            overview = "",
            posterPath = null,
            runtime = null
        ).toDomain(CreditsResponse())

        assertEquals("Sin fecha", result.year)
        assertEquals("No disponible", result.director)
        assertEquals("No disponible", result.runtimeLabel)
    }
}
