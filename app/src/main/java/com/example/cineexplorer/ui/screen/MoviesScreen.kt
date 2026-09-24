package com.example.cineexplorer.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.cineexplorer.data.model.MovieSummary
import com.example.cineexplorer.ui.viewmodel.MoviesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoviesScreen(
    genreName: String,
    viewModel: MoviesViewModel,
    onBack: () -> Unit,
    onMovieSelected: (MovieSummary) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(genreName.ifBlank { "Películas" }) },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text("Atrás") }
                }
            )
        }
    ) { padding ->
        when {
            state.isLoading -> LoadingContent(Modifier.padding(padding))
            state.movies.isEmpty() && state.error != null -> MessageContent(
                message = requireNotNull(state.error),
                modifier = Modifier.padding(padding),
                actionLabel = "Reintentar",
                onAction = viewModel::loadFirstPage
            )
            state.movies.isEmpty() -> MessageContent(
                message = "No se encontraron películas para este género.",
                modifier = Modifier.padding(padding)
            )
            else -> LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(state.movies, key = { it.id }) { movie ->
                    MovieCard(movie = movie, onClick = { onMovieSelected(movie) })
                }
                loadMoreItem(
                    canLoadMore = state.canLoadMore,
                    isLoading = state.isLoadingMore,
                    error = state.error,
                    onLoadMore = viewModel::loadMore
                )
            }
        }
    }
}

private fun LazyGridScope.loadMoreItem(
    canLoadMore: Boolean,
    isLoading: Boolean,
    error: String?,
    onLoadMore: () -> Unit
) {
    if (canLoadMore || isLoading || error != null) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (error != null) {
                    Text(error, style = MaterialTheme.typography.bodyMedium)
                }
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.padding(12.dp))
                } else if (canLoadMore) {
                    Button(onClick = onLoadMore, modifier = Modifier.padding(top = 8.dp)) {
                        Text(if (error == null) "Cargar más" else "Reintentar")
                    }
                }
            }
        }
    }
}

@Composable
private fun MovieCard(movie: MovieSummary, onClick: () -> Unit) {
    Card(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Column {
            if (movie.posterUrl == null) {
                Box(
                    modifier = Modifier.fillMaxWidth().aspectRatio(2f / 3f),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Póster no disponible")
                }
            } else {
                AsyncImage(
                    model = movie.posterUrl,
                    contentDescription = "Póster de ${movie.title}",
                    modifier = Modifier.fillMaxWidth().aspectRatio(2f / 3f),
                    contentScale = ContentScale.Crop
                )
            }
            Text(
                text = movie.title,
                modifier = Modifier.padding(start = 12.dp, top = 10.dp, end = 12.dp),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "${movie.year} · ${String.format("%.1f", movie.voteAverage)}/10",
                modifier = Modifier.padding(start = 12.dp, top = 4.dp, end = 12.dp, bottom = 12.dp),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
