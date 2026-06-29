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

    val navController = rememberNavController()

    val userViewModel: UserViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
    ) {

        // ==========================================
        // AUTENTICACIÓN Y PERFIL
        // ==========================================

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

        composable(Routes.SEARCH_USER) { // Asegúrate de agregar const val SEARCH_USER = "search_user" en tus Routes si no lo has hecho
            SearchUserScreen(navController = navController)
        }

        composable(
            route = Routes.PROFILE,
            arguments = listOf(
                androidx.navigation.navArgument("usuarioId") {
                    type = androidx.navigation.NavType.IntType // 🔥 Cambiado a IntType para que coincida con tus ViewModels
                    defaultValue = -1 // 🔥 Usamos -1 como bandera para "Mi Perfil"
                }
            )
        ) { backStackEntry ->
            // Jetpack Navigation retornará el defaultValue (-1) si no se pasa ningún parámetro en la URL
            val usuarioId = backStackEntry.arguments?.getInt("usuarioId") ?: -1
            val finalId = if (usuarioId == -1) null else usuarioId

            ProfileScreen(navController = navController, usuarioId = finalId)
        }

        composable(Routes.EDIT_PROFILE) {
            EditProfileScreen(navController = navController)
        }

        // ==========================================
        // FUNCIONALIDADES PRINCIPALES (MICHIMAKER)
        // ==========================================

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

        composable(Routes.RESULT) { backStackEntry ->
            val image = Uri.decode(
                backStackEntry.arguments?.getString("image") ?: ""
            )
            val filter = Uri.decode(
                backStackEntry.arguments?.getString("filter") ?: ""
            )

            ResultScreen(
                navController = navController,
                image = image,
                filter = filter
            )
        }

        // ==========================================
        // SECCIONES DE SOPORTE / PANEL
        // ==========================================

        composable(Routes.HISTORY) {
            HistoryScreen(navController)
        }

        composable(Routes.STATS) {
            StatsScreen(navController)
        }

        composable(Routes.SETTINGS) {
            SettingsScreen(navController)
        }

        // ==========================================
        // SISTEMA DE CHATS Y MENSAJERÍA REAL
        // ==========================================

        // Bandeja de entrada (Lista de chats activos)
        composable(Routes.CHATS_LIST) {
            ChatsListScreen(navController = navController)
        }

        // Pantalla de Chat Individual (Ruta única oficial)
        composable(
            route = Routes.CHAT,
            arguments = listOf(
                androidx.navigation.navArgument("otroUsuarioId") { type = androidx.navigation.NavType.IntType },
                androidx.navigation.navArgument("otroUsername") { type = androidx.navigation.NavType.StringType }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("otroUsuarioId") ?: 0
            val username = backStackEntry.arguments?.getString("otroUsername") ?: ""

            ChatScreen(
                navController = navController,
                otroUsuarioId = id,
                otroUsername = username
            )
        }
    }
}