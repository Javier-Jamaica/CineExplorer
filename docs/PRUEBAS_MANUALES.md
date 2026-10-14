# Pruebas manuales de CineExplorer 1.0.1

Estos casos son un plan de aceptación. Registre los resultados reales después de ejecutarlos en Android Studio con su token; no representan pruebas ya realizadas.

| Caso | Procedimiento | Resultado esperado |
|---|---|---|
| Acceso válido | Configure `TMDB_BEARER_TOKEN`, sincronice y ejecute con Internet. | Se muestran los géneros de TMDB. |
| Filtro | Seleccione Acción y posteriormente otro género. | Las películas corresponden al género seleccionado. |
| Pósteres | Abra un género y espere a que carguen las imágenes. | Aparece el póster de cada película que tiene `poster_path`. |
| Detalle | Abra una película. | La ficha presenta su póster, año, director o directores y sinopsis. |
| Imagen ausente | Revise una película sin `poster_path`. | Se informa que TMDB no ofrece un póster. |
| Reintento de imagen | Desactive Internet antes de cargar una imagen, restáurelo y pulse Reintentar imagen. | Se vuelve a solicitar el póster, conservando la lista. |
| Paginación | Pulse Cargar más al final del listado. | Se añaden resultados sin duplicados. |
| Preferencias | Oculte un género, guarde y vuelva a abrir. | Se conserva la selección. |
| Restauración | Pulse Restaurar valores predeterminados. | Se muestran todos los géneros disponibles. |
| Token ausente | Quite el token, recompile y ejecute. | Se indica que falta configurar el acceso al catálogo. |
| Token inválido | Configure un valor inválido, recompile y ejecute. | Aparece un error recuperable de acceso. |
| Sin Internet | Desactive Internet antes de solicitar el catálogo. | Se informa del problema y se ofrece reintentar. |

## Registro de resultados

- Equipo o emulador: pendiente.
- Versión de Android: pendiente.
- Fecha de ejecución: pendiente.
- Compilación: pendiente.
- Pruebas unitarias: pendiente.
- Casos manuales: pendiente.

El token nunca debe aparecer en capturas, resultados ni commits.
