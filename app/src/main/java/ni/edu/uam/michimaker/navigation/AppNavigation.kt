package ni.edu.uam.michimaker.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ni.edu.uam.michimaker.screens.*
import androidx.lifecycle.viewmodel.compose.viewModel
import ni.edu.uam.michimaker.viewmodel.UserViewModel

@Composable
fun AppNavigation() {

    val navController =
        rememberNavController()

    val userViewModel: UserViewModel =
        viewModel()

    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
    ) {

        // =====================
        // AUTENTICACIÓN
        // =====================

        composable(Routes.LOGIN) {

            LoginScreen(
                navController = navController,
                viewModel = userViewModel
            )
        }

        composable(Routes.REGISTER) {

            RegisterScreen(
                navController = navController,
                viewModel = userViewModel
            )
        }

        // =====================
        // APP
        // =====================

        composable(Routes.HOME) {
            HomeScreen(navController)
        }

        composable(Routes.CAMERA) {
            CameraScreen(navController)
        }

        composable(Routes.FILTER) { backStackEntry ->

            val imagePath =
                Uri.decode(
                    backStackEntry.arguments
                        ?.getString("image")
                        ?: ""
                )

            FilterScreen(
                navController = navController,
                imagePath = imagePath
            )
        }

        composable(Routes.RESULT) { backStackEntry ->

            val image =
                Uri.decode(
                    backStackEntry.arguments
                        ?.getString("image")
                        ?: ""
                )

            val filter =
                Uri.decode(
                    backStackEntry.arguments
                        ?.getString("filter")
                        ?: ""
                )

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