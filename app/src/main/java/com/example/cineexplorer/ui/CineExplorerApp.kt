package com.example.cineexplorer.ui

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.cineexplorer.ui.screen.AdminScreen
import com.example.cineexplorer.ui.screen.DetailScreen
import com.example.cineexplorer.ui.screen.GenresScreen
import com.example.cineexplorer.ui.screen.MoviesScreen
import com.example.cineexplorer.ui.viewmodel.AdminViewModel
import com.example.cineexplorer.ui.viewmodel.DetailViewModel
import com.example.cineexplorer.ui.viewmodel.GenresViewModel
import com.example.cineexplorer.ui.viewmodel.MoviesViewModel

private object Routes {
    const val GENRES = "genres"
    const val ADMIN = "admin"
    const val MOVIES = "movies/{genreId}/{genreName}"
    const val DETAIL = "detail/{movieId}"

    fun movies(genreId: Int, genreName: String) = "movies/$genreId/${Uri.encode(genreName)}"
    fun detail(movieId: Int) = "detail/$movieId"
}

@Composable
fun CineExplorerApp() {
    val navController = rememberNavController()
    val appContext = LocalContext.current.applicationContext
    val container = remember(appContext) { AppContainer(appContext) }

    NavHost(navController = navController, startDestination = Routes.GENRES) {
        composable(Routes.GENRES) {
            val factory = remember(container) {
                SimpleViewModelFactory {
                    GenresViewModel(container.movieRepository, container.settingsRepository)
                }
            }
            val screenModel: GenresViewModel = viewModel(factory = factory)
            GenresScreen(
                viewModel = screenModel,
                onGenreSelected = { genre ->
                    navController.navigate(Routes.movies(genre.id, genre.name))
                },
                onOpenAdmin = { navController.navigate(Routes.ADMIN) }
            )
        }

        composable(
            route = Routes.MOVIES,
            arguments = listOf(
                navArgument("genreId") { type = NavType.IntType },
                navArgument("genreName") { type = NavType.StringType }
            )
        ) { entry ->
            val genreId = requireNotNull(entry.arguments?.getInt("genreId"))
            val genreName = Uri.decode(entry.arguments?.getString("genreName").orEmpty())
            val factory = remember(container, genreId) {
                SimpleViewModelFactory { MoviesViewModel(container.movieRepository, genreId) }
            }
            val screenModel: MoviesViewModel = viewModel(factory = factory)
            MoviesScreen(
                genreName = genreName,
                viewModel = screenModel,
                onBack = navController::navigateUp,
                onMovieSelected = { navController.navigate(Routes.detail(it.id)) }
            )
        }

        composable(
            route = Routes.DETAIL,
            arguments = listOf(navArgument("movieId") { type = NavType.IntType })
        ) { entry ->
            val movieId = requireNotNull(entry.arguments?.getInt("movieId"))
            val factory = remember(container, movieId) {
                SimpleViewModelFactory { DetailViewModel(container.movieRepository, movieId) }
            }
            val screenModel: DetailViewModel = viewModel(factory = factory)
            DetailScreen(viewModel = screenModel, onBack = navController::navigateUp)
        }

        composable(Routes.ADMIN) {
            val factory = remember(container) {
                SimpleViewModelFactory {
                    AdminViewModel(container.movieRepository, container.settingsRepository)
                }
            }
            val screenModel: AdminViewModel = viewModel(factory = factory)
            AdminScreen(viewModel = screenModel, onBack = navController::navigateUp)
        }
    }
}
