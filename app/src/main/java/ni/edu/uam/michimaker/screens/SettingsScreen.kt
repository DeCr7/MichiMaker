package ni.edu.uam.michimaker.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ni.edu.uam.michimaker.database.AppDatabaseProvider
import ni.edu.uam.michimaker.navigation.Routes
import ni.edu.uam.michimaker.repository.TransformacionRepository
import ni.edu.uam.michimaker.utils.SessionManager
import ni.edu.uam.michimaker.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(
    navController: NavController
) {
    val gradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFD7CCC8),
            Color(0xFFBCAAA4),
            Color(0xFFEFEBE9)
        )
    )

    val usuario =
        SessionManager.obtenerUsuario()

    if (usuario == null) {

        LaunchedEffect(Unit) {

            navController.navigate(
                Routes.LOGIN
            ) {
                popUpTo(0)
            }
        }

        return
    }

    val context = LocalContext.current

    val repository = remember {

        TransformacionRepository(
            AppDatabaseProvider
                .obtener(context)
                .transformacionDao()
        )
    }

    val viewModel = remember {

        SettingsViewModel(
            repository
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
            .padding(16.dp)
    ) {

        Text(
            text = "Configuración",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = "Usuario: ${usuario.username}"
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {

            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                Text("Historial")

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Button(
                    onClick = {

                        viewModel
                            .limpiarHistorialUsuario(
                                usuario.id ?: 0
                            ) {

                                navController.navigate(
                                    Routes.HOME
                                ) {
                                    launchSingleTop = true
                                    popUpTo(
                                        Routes.HOME
                                    )
                                }
                            }
                    }
                ) {

                    Text(
                        "Limpiar mi historial"
                    )
                }
            }
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {

            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                Text("MichiMaker")
                Text("Versión 1.0")
                Text("Filtros felinos con ML Kit")
            }
        }

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Button(
            onClick = {

                navController.navigate(
                    Routes.HOME
                ) {
                    launchSingleTop = true
                    popUpTo(
                        Routes.HOME
                    )
                }
            }
        ) {

            Text(
                "Volver al inicio"
            )
        }

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        OutlinedButton(
            onClick = {

                SessionManager.logout()

                navController.navigate(
                    Routes.LOGIN
                ) {

                    popUpTo(0)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                "Cerrar sesión"
            )
        }
    }
}