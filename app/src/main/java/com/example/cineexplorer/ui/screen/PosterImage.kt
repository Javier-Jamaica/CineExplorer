package com.example.cineexplorer.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest

/** Carga el póster y permite reintentar sin recargar todo el catálogo. */
@Composable
fun PosterImage(
    posterUrl: String?,
    title: String,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    val context = LocalContext.current
    val url = posterUrl?.takeIf { it.isNotBlank() }
    var attempt by remember(url) { mutableStateOf(0) }
    var status by remember(url, attempt) {
        mutableStateOf(if (url == null) PosterStatus.UNAVAILABLE else PosterStatus.LOADING)
    }
    val request = remember(context, url) {
        ImageRequest.Builder(context).data(url).crossfade(true).build()
    }

    Box(
        modifier = modifier.background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center
    ) {
        if (url != null) {
            key(url, attempt) {
                AsyncImage(
                    model = request,
                    contentDescription = "Póster de $title",
                    modifier = Modifier.matchParentSize(),
                    contentScale = contentScale,
                    onLoading = { status = PosterStatus.LOADING },
                    onSuccess = { status = PosterStatus.READY },
                    onError = { status = PosterStatus.ERROR }
                )
            }
        }

        when (status) {
            PosterStatus.LOADING -> CircularProgressIndicator()
            PosterStatus.READY -> Unit
            PosterStatus.ERROR, PosterStatus.UNAVAILABLE -> Column(
                modifier = Modifier.fillMaxSize().padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = if (status == PosterStatus.UNAVAILABLE) {
                        "TMDB no ofrece un póster para esta película."
                    } else {
                        "No se pudo cargar el póster."
                    },
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodySmall
                )
                if (status == PosterStatus.ERROR) {
                    TextButton(onClick = { attempt += 1 }) {
                        Text("Reintentar imagen")
                    }
                }
            }
        }
    }
}

private enum class PosterStatus { LOADING, READY, ERROR, UNAVAILABLE }
