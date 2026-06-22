package ni.edu.uam.michimaker.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ni.edu.uam.michimaker.navigation.Routes
import ni.edu.uam.michimaker.repository.TransformacionRepository
import ni.edu.uam.michimaker.utils.SessionManager
import ni.edu.uam.michimaker.utils.StatItem
import ni.edu.uam.michimaker.viewmodel.StatsScreenViewModel

@Composable
fun StatsScreen(
    navController: NavController
) {

    val gradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFFFAB91),
            Color(0xFFFFCC80),
            Color(0xFFFF8A65)
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

    val repository = remember {
        TransformacionRepository()
    }

    val viewModel = remember {

        StatsScreenViewModel(
            repository = repository,
            usuarioId = usuario.id ?: 0
        )
    }

    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
            .padding(16.dp)
    ) {

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

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Text(
            text = "Estadísticas de ${usuario.username}",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

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

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Text(
            text = "Por filtro",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

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

                Spacer(
                    modifier = Modifier.height(8.dp)
                )
            }
        }
    }
}