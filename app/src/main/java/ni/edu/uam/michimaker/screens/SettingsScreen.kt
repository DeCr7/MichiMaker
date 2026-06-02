package ni.edu.uam.michimaker.screens

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ni.edu.uam.michimaker.viewmodel.SettingsViewModel
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ni.edu.uam.michimaker.navigation.Routes

@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel = viewModel()
) {

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

        // ---------------- LIMPIAR HISTORIAL ----------------
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {

            Column(modifier = Modifier.padding(16.dp)) {

                Text("Historial")

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        viewModel.limpiarHistorial {
                            navController.navigate(Routes.HOME)
                        }
                    }
                ) {
                    Text("Limpiar historial")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ---------------- INFO APP ----------------
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {

            Column(modifier = Modifier.padding(16.dp)) {

                Text("MichiMaker")
                Text("Versión 1.0")
                Text("Filtro de rostros con ML Kit + overlays")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                navController.navigate(Routes.HOME)
            }
        ) {
            Text("Volver al inicio")
        }
    }
}