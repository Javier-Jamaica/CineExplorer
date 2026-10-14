# Registro de cambios

Todos los cambios importantes de CineExplorer se documentan en este archivo. El formato se basa en [Keep a Changelog](https://keepachangelog.com/es-ES/1.1.0/) y el proyecto utiliza [versionado semántico](https://semver.org/lang/es/).

## [No publicado]

Pendiente: ejecutar la compilación y las pruebas de aceptación en Android Studio con un token válido.

## [1.0.1] - 2026-10-14

### Corregido

- Se retiró el catálogo ficticio y se restauró TMDB como única fuente.
- Las imágenes utilizan las rutas de pósteres proporcionadas por la API.
- Los pósteres cuentan con carga, mensaje de error y reintento individual.
- Se normaliza el token y se identifica su ausencia antes de enviar solicitudes.
- La ficha muestra los directores sin duplicados.
- El Gradle Wrapper utiliza la versión 8.13.

### Agregado

- Pruebas de regresión de URLs y películas con varios directores.
- Guía de configuración del API Read Access Token y recompilación.

### Validación

- Revisión del código, configuración y estructura del paquete.
- La compilación y las pruebas Android/JUnit están pendientes de ejecutarse en Android Studio.

## [1.0.0] - 2026-10-14

### Agregado

- Catálogo local con ocho películas distribuidas en siete géneros.
- Funcionamiento inmediato sin cuenta, token o conexión a Internet.
- Pruebas unitarias del catálogo local.
- Estado visual alternativo para películas sin póster.

### Cambiado

- TMDB ahora es una integración opcional que se activa mediante `local.properties`.
- Se actualizó la documentación, la guía de pruebas y la versión de la aplicación.

### Verificado

- La estructura incluye código, documentación, wireframe y Gradle Wrapper.
- `.gitignore` excluye credenciales, configuraciones locales y archivos generados.
- No hay claves o tokens almacenados en el código fuente.

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
