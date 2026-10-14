package com.example.cineexplorer.ui.state

import com.example.cineexplorer.data.remote.MissingTmdbTokenException
import retrofit2.HttpException
import java.io.IOException

sealed interface LoadState<out T> {
    data object Loading : LoadState<Nothing>
    data class Success<T>(val data: T) : LoadState<T>
    data class Error(val message: String) : LoadState<Nothing>
}

fun Throwable.toUserMessage(): String = when (this) {
    is MissingTmdbTokenException -> "Falta configurar el acceso al catálogo de películas."
    is HttpException -> when (code()) {
        401, 403 -> "El acceso al catálogo fue rechazado. Revisa la configuración de la cuenta."
        else -> "El catálogo no está disponible en este momento. Inténtalo nuevamente."
    }
    is IOException -> "No se pudo conectar. Revisa tu conexión a Internet e inténtalo de nuevo."
    else -> "No fue posible obtener la información. Inténtalo nuevamente."
}
