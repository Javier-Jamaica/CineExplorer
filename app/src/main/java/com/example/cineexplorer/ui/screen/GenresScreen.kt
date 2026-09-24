package com.example.cineexplorer.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cineexplorer.data.model.Genre
import com.example.cineexplorer.ui.state.LoadState
import com.example.cineexplorer.ui.viewmodel.GenresViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenresScreen(
    viewModel: GenresViewModel,
    onGenreSelected: (Genre) -> Unit,
    onOpenAdmin: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CineExplorer") },
                actions = {
                    TextButton(onClick = onOpenAdmin) { Text("Administrar") }
                }
            )
        }
    ) { padding ->
        when (val current = state) {
            LoadState.Loading -> LoadingContent(Modifier.padding(padding))
            is LoadState.Error -> MessageContent(
                message = current.message,
                modifier = Modifier.padding(padding),
                actionLabel = "Reintentar",
                onAction = viewModel::load
            )
            is LoadState.Success -> {
                if (current.data.isEmpty()) {
                    MessageContent(
                        message = "No hay géneros visibles. Revisa la configuración administrativa.",
                        modifier = Modifier.padding(padding),
                        actionLabel = "Abrir configuración",
                        onAction = onOpenAdmin
                    )
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 150.dp),
                        modifier = Modifier.fillMaxSize().padding(padding),
                        contentPadding = PaddingValues(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(current.data, key = { it.id }) { genre ->
                            GenreCard(genre = genre, onClick = { onGenreSelected(genre) })
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun GenreCard(genre: Genre, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = genre.name.take(1).uppercase(),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = genre.name,
                modifier = Modifier.padding(top = 8.dp),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}
