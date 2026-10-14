package com.example.cineexplorer.data.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
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

    @Test
    fun `posterUrl se construye igual para tarjeta y ficha`() {
        val summary = MovieSummary(10, "Película", "/poster.jpg")
        val detail = MovieDetailsDto(
            id = 10,
            title = "Película",
            posterPath = "/poster.jpg",
            runtime = null
        ).toDomain(CreditsResponse())

        assertEquals("https://image.tmdb.org/t/p/w500/poster.jpg", summary.posterUrl)
        assertEquals(summary.posterUrl, detail.posterUrl)
    }

    @Test
    fun `posterUrl maneja ruta vacia y URL completa`() {
        assertNull(null.toPosterUrl())
        assertNull("  ".toPosterUrl())
        assertEquals("https://image.tmdb.org/t/p/w500/poster.jpg", "poster.jpg".toPosterUrl())
        assertEquals("https://example.com/poster.jpg", "https://example.com/poster.jpg".toPosterUrl())
    }

    @Test
    fun `ficha muestra todos los directores sin duplicarlos`() {
        val details = MovieDetailsDto(10, "Película", posterPath = null, runtime = null)
        val credits = CreditsResponse(crew = listOf(
            CrewMember("Directora A", "Director"),
            CrewMember("Director B", "Director"),
            CrewMember("Directora A", "Director"),
            CrewMember("Guionista", "Writer")
        ))

        assertEquals("Directora A, Director B", details.toDomain(credits).director)
    }
}
