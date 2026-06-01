package ni.edu.uam.michimaker.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigation() {

    val navController =
        rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.HOME
    ) {

        composable(Routes.HOME) {
            HomeScreen(navController)
        }

        composable(Routes.CAMERA) {
            CameraScreen(navController)
        }

        composable(Routes.FILTER) {
            FilterScreen(navController)
        }

        composable(Routes.RESULT) {
            ResultScreen(navController)
        }

        composable(Routes.HISTORY) {
            HistoryScreen(navController)
        }

        composable(Routes.STATS) {
            StatsScreen(navController)
        }

        composable(Routes.SETTINGS) {
            SettingsScreen(navController)
        }
    }
}