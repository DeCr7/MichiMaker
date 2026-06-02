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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ni.edu.uam.michimaker.database.AppDatabaseProvider
import ni.edu.uam.michimaker.navigation.Routes
import ni.edu.uam.michimaker.repository.TransformacionRepository
import ni.edu.uam.michimaker.utils.StatItem
import ni.edu.uam.michimaker.viewmodel.StatsViewModel

@Composable
fun StatsScreen(
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
        StatsViewModel(repository)
    }

    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Button(
            onClick = {
                navController.navigate(Routes.HOME) {
                    launchSingleTop = true
                    popUpTo(Routes.HOME)
                }
            }
        ) {
            Text("Volver al inicio")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Estadísticas",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {

            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                Text(
                    text = "Total de transformaciones"
                )

                Text(
                    text = state.total.toString(),
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Por filtro",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (state.porFiltro.isEmpty()) {

            Text(
                text = "No hay datos aún"
            )

        } else {

            state.porFiltro.forEach { (filtro, cantidad) ->

                StatItem(
                    filtro = filtro,
                    cantidad = cantidad
                )
            }
        }
    }
}