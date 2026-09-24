package com.example.cineexplorer.ui.state

sealed interface LoadState<out T> {
    data object Loading : LoadState<Nothing>
    data class Success<T>(val data: T) : LoadState<T>
    data class Error(val message: String) : LoadState<Nothing>
}

fun Throwable.toUserMessage(): String = when {
    message?.contains("401") == true ->
        "TMDB rechazó la solicitud. Verifica el valor TMDB_BEARER_TOKEN en local.properties."
    message?.contains("Unable to resolve host") == true ->
        "No se pudo conectar. Revisa tu conexión a Internet e inténtalo de nuevo."
    else -> "No fue posible obtener la información. Inténtalo nuevamente."
}
