package com.example.cineexplorer.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.cineexplorer.data.model.MovieDetail
import com.example.cineexplorer.ui.state.LoadState
import com.example.cineexplorer.ui.viewmodel.DetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(viewModel: DetailViewModel, onBack: () -> Unit) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de película") },
                navigationIcon = { TextButton(onClick = onBack) { Text("Atrás") } }
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
            is LoadState.Success -> MovieDetailContent(
                detail = current.data,
                modifier = Modifier.padding(padding)
            )
        }
    }
}

@Composable
private fun MovieDetailContent(detail: MovieDetail, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            AsyncImage(
                model = detail.posterUrl,
                contentDescription = "Póster de ${detail.title}",
                modifier = Modifier.fillMaxWidth().heightIn(min = 320.dp, max = 520.dp),
                contentScale = ContentScale.Crop
            )
        }
        item {
            Column(
                modifier = Modifier.padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = detail.title,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text(detail.year)
                    Text(detail.runtimeLabel)
                    Text(detail.ratingLabel)
                }
                DetailField("Géneros", detail.genresLabel)
                DetailField("Director", detail.director)
                DetailField("Reparto principal", detail.castLabel)
                DetailField("Sinopsis", detail.overview)
            }
        }
        item { Text("", modifier = Modifier.padding(bottom = 16.dp)) }
    }
}

@Composable
private fun DetailField(label: String, value: String) {
    Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
        Text(label, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
        Text(value, style = MaterialTheme.typography.bodyLarge)
    }
}
