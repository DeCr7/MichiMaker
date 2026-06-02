package ni.edu.uam.michimaker.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ni.edu.uam.michimaker.database.AppDatabaseProvider
import ni.edu.uam.michimaker.navigation.Routes
import ni.edu.uam.michimaker.repository.TransformacionRepository
import ni.edu.uam.michimaker.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(
    navController: NavController
) {

    val context = LocalContext.current

    val repository = remember {
        TransformacionRepository(
            AppDatabaseProvider
                .obtener(context)
                .transformacionDao()
        )
    }

    val viewModel = remember {
        SettingsViewModel(repository)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Configuración",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {

            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                Text("Historial")

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        viewModel.limpiarHistorial {

                            navController.navigate(
                                Routes.HOME
                            ) {
                                launchSingleTop = true
                                popUpTo(Routes.HOME)
                            }
                        }
                    }
                ) {
                    Text("Limpiar historial")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

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

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                navController.navigate(
                    Routes.HOME
                ) {
                    launchSingleTop = true
                    popUpTo(Routes.HOME)
                }
            }
        ) {
            Text("Volver al inicio")
        }
    }
}