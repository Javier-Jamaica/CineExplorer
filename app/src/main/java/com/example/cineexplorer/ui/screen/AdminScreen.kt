package com.example.cineexplorer.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cineexplorer.data.model.Genre
import com.example.cineexplorer.ui.viewmodel.AdminViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(viewModel: AdminViewModel, onBack: () -> Unit) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Administrar géneros") },
                navigationIcon = { TextButton(onClick = onBack) { Text("Atrás") } }
            )
        }
    ) { padding ->
        when {
            state.isLoading -> LoadingContent(Modifier.padding(padding))
            state.error != null -> MessageContent(
                message = requireNotNull(state.error),
                modifier = Modifier.padding(padding),
                actionLabel = "Reintentar",
                onAction = viewModel::load
            )
            else -> LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
            ) {
                item {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            "Elige los géneros visibles y marca uno como destacado.",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            "Debe permanecer al menos un género activo.",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
                items(state.genres, key = { it.id }) { genre ->
                    GenreSettingRow(
                        genre = genre,
                        isVisible = genre.id in state.selectedIds,
                        isFeatured = genre.id == state.featuredId,
                        onToggle = { viewModel.toggleGenre(genre.id) },
                        onFeatured = { viewModel.selectFeatured(genre.id) }
                    )
                    Divider()
                }
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        state.confirmation?.let {
                            Text(it, color = MaterialTheme.colorScheme.primary)
                        }
                        Button(onClick = viewModel::save, modifier = Modifier.fillMaxWidth()) {
                            Text("Guardar configuración")
                        }
                        TextButton(onClick = viewModel::reset) {
                            Text("Restaurar valores predeterminados")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun GenreSettingRow(
    genre: Genre,
    isVisible: Boolean,
    isFeatured: Boolean,
    onToggle: () -> Unit,
    onFeatured: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(genre.name, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyLarge)
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Destacado", style = MaterialTheme.typography.labelSmall)
            RadioButton(
                selected = isFeatured,
                onClick = onFeatured,
                enabled = isVisible
            )
        }
        Column(
            modifier = Modifier.clickable(onClick = onToggle),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Visible", style = MaterialTheme.typography.labelSmall)
            Switch(checked = isVisible, onCheckedChange = { onToggle() })
        }
    }
}
