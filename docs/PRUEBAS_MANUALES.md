# Pruebas manuales de CineExplorer

| Caso | Procedimiento | Resultado esperado |
|---|---|---|
| Inicio correcto | Abrir la aplicación con Internet y un token válido. | Se muestran los géneros de TMDB. |
| Filtro por género | Seleccionar un género. | Aparece una cuadrícula de películas relacionadas. |
| Detalle | Seleccionar una película. | Se muestran año, director, géneros, sinopsis, reparto, duración y calificación. |
| Paginación | Pulsar **Cargar más** al final de la cuadrícula. | Se agregan películas sin duplicar las anteriores. |
| Persistencia | Desactivar un género, guardar, cerrar y volver a abrir. | El género permanece oculto. |
| Restauración | Pulsar **Restaurar valores predeterminados**. | Todos los géneros vuelven a estar visibles. |
| Sin token | Eliminar `TMDB_BEARER_TOKEN` y abrir la app. | Se muestra un error y la aplicación no se cierra. |
| Sin Internet | Desconectar el dispositivo y reintentar. | Se presenta un mensaje con opción de reintento. |
