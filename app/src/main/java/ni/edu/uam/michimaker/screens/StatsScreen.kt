package ni.edu.uam.michimaker.screens

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ni.edu.uam.michimaker.viewmodel.StatsViewModel
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.getValue
import ni.edu.uam.michimaker.navigation.Routes
import ni.edu.uam.michimaker.utils.StatItem

@Composable
fun StatsScreen(
    navController: NavController,
    viewModel: StatsViewModel = viewModel()
) {

    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Estadísticas",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        // ---------------- TOTAL ----------------
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                Text("Total de transformaciones")
                Text(
                    text = state.total.toString(),
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ---------------- POR FILTRO ----------------
        Text(
            text = "Por filtro",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (state.porFiltro.isEmpty()) {

            Text("No hay datos aún")

        } else {

            state.porFiltro.forEach { (filtro, cantidad) ->

                StatItem(filtro, cantidad)
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