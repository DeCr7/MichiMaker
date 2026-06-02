package ni.edu.uam.michimaker.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import ni.edu.uam.michimaker.utils.TransformacionItem
import ni.edu.uam.michimaker.viewmodel.TransformacionViewModel

@Composable
fun HistoryScreen(
    navController: NavController
) {

    val gradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFFFC1CC), // rosa suave
            Color(0xFFD7B3FF), // lila
            Color(0xFFFFD6A5)  // naranja pastel
        )
    )

    val context = LocalContext.current

    val repository = remember {
        TransformacionRepository(
            AppDatabaseProvider
                .obtener(context)
                .transformacionDao()
        )
    }

    val viewModel = remember {
        TransformacionViewModel(repository)
    }

    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient) // ✔ SOLO agregado aquí
            .padding(16.dp)
    ) {

        Text(
            text = "Historial",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Button(
            onClick = {
                navController.navigate(Routes.HOME) {
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
            text = "Total de transformaciones: ${state.total}",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        when {

            state.cargando -> {

                CircularProgressIndicator()
            }

            state.error != null -> {

                Text(
                    text = "Error: ${state.error}",
                    color = MaterialTheme.colorScheme.error
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                OutlinedButton(
                    onClick = {
                        navController.navigate(Routes.HOME)
                    }
                ) {
                    Text("Volver")
                }
            }

            state.transformaciones.isEmpty() -> {

                Text(
                    text = "No hay transformaciones registradas."
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                OutlinedButton(
                    onClick = {
                        navController.navigate(Routes.CAMERA)
                    }
                ) {
                    Text("Crear una transformación")
                }
            }

            else -> {

                LazyColumn(
                    verticalArrangement =
                        Arrangement.spacedBy(12.dp)
                ) {

                    items(
                        items = state.transformaciones,
                        key = { it.id }
                    ) { item ->

                        TransformacionItem(
                            item = item,
                            onDelete = {
                                viewModel.eliminar(item)
                            }
                        )
                    }
                }
            }
        }
    }
}