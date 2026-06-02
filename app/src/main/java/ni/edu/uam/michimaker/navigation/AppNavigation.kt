package ni.edu.uam.michimaker.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ni.edu.uam.michimaker.screens.CameraScreen
import ni.edu.uam.michimaker.screens.FilterScreen
import ni.edu.uam.michimaker.screens.HistoryScreen
import ni.edu.uam.michimaker.screens.HomeScreen
import ni.edu.uam.michimaker.screens.ResultScreen
import ni.edu.uam.michimaker.screens.SettingsScreen
import ni.edu.uam.michimaker.screens.StatsScreen

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

        composable(Routes.FILTER) { backStackEntry ->

            val imagePath = Uri.decode(
                backStackEntry.arguments?.getString("image") ?: ""
            )

            FilterScreen(
                navController = navController,
                imagePath = imagePath
            )
        }

        composable("result/{image}/{filter}") { backStackEntry ->

            val image = Uri.decode(backStackEntry.arguments?.getString("image") ?: "")
            val filter = Uri.decode(backStackEntry.arguments?.getString("filter") ?: "")

            ResultScreen(
                navController = navController,
                image = image,
                filter = filter
            )
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