# Registro de cambios

Todos los cambios importantes de CineExplorer se documentan en este archivo. El formato se basa en [Keep a Changelog](https://keepachangelog.com/es-ES/1.1.0/) y el proyecto utiliza [versionado semántico](https://semver.org/lang/es/).

## [No publicado]

### Por agregar

- Pruebas instrumentadas de navegación y estados de interfaz.
- Revisión de accesibilidad y validación en diferentes pantallas.
- Evidencia final de pruebas y APK verificado.

## [0.2.1] - 2026-09-22

### Corregido

- Se habilitó la sincronización desde rutas de Windows con caracteres no ASCII.
- Se documentó como solución recomendada mover el proyecto a una ruta sin tildes.

## [0.2.0] - 2026-09-22

### Agregado

- Proyecto Android Studio con Kotlin y Jetpack Compose.
- Consumo de géneros, descubrimiento, detalles y créditos de TMDB.
- Pantallas de géneros, películas, detalle y administración.
- Paginación, carga de pósteres, reintentos y estados vacíos.
- Preferencias locales con DataStore.
- Pruebas unitarias del mapeo y plan de pruebas manuales.

### Cambiado

- El borrador ahora documenta una versión funcional.
- La ficha integra los créditos para presentar director y reparto.
- El README incluye instalación, arquitectura y publicación.

### Seguridad

- El token de TMDB se lee desde `local.properties`, excluido por `.gitignore`.
- El registro de red se desactiva fuera de la compilación de depuración.

## [0.1.0] - 2026-08-26

### Agregado

- Descripción, problema, objetivos, plataforma y alcance.
- Selección de TMDB como fuente de información.
- Wireframes y flujo de navegación inicial.
