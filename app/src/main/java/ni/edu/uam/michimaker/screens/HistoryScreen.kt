package ni.edu.uam.michimaker.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ni.edu.uam.michimaker.navigation.Routes
import ni.edu.uam.michimaker.repository.TransformacionRepository
import ni.edu.uam.michimaker.utils.ImageUtils
import ni.edu.uam.michimaker.utils.SessionManager
import ni.edu.uam.michimaker.viewmodel.TransformacionViewModel
import ni.edu.uam.michimaker.viewmodel.TransformacionViewModelFactory

@Composable
fun HistoryScreen(
    navController: NavController
) {

    val gradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFFFC1CC),
            Color(0xFFD7B3FF),
            Color(0xFFFFD6A5)
        )
    )

    val usuario = SessionManager.obtenerUsuario()

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

    val viewModel: TransformacionViewModel =
        viewModel(
            factory =
                TransformacionViewModelFactory(
                    repository = repository,
                    usuarioId = usuario.id ?: 0
                )
        )

    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
            .padding(16.dp)
    ) {

        Text(
            text = "Historial de ${usuario.username}",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Text(
            text = "Transformaciones guardadas en tu cuenta",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Button(
            onClick = {
                navController.navigate(
                    Routes.HOME
                ) {
                    launchSingleTop = true
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
            }

            state.transformaciones.isEmpty() -> {

                Text(
                    text = "No hay transformaciones registradas."
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Button(
                    onClick = {
                        navController.navigate(
                            Routes.CAMERA
                        )
                    }
                ) {
                    Text("Crear una Transformación")
                }
            }

            else -> {

                LazyColumn(
                    verticalArrangement =
                        Arrangement.spacedBy(12.dp)
                ) {

                    items(
                        items = state.transformaciones,
                        key = { it.id ?: 0 }
                    ) { item ->

                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {

                            Column(
                                modifier = Modifier.padding(12.dp)
                            ) {

                                Text(
                                    text =
                                        item.nombreFiltro
                                            ?: "Sin filtro",

                                    style =
                                        MaterialTheme
                                            .typography
                                            .titleMedium
                                )

                                Spacer(
                                    modifier =
                                        Modifier.height(6.dp)
                                )

                                Text(
                                    text =
                                        "Fecha: ${item.fecha ?: ""}",

                                    style =
                                        MaterialTheme
                                            .typography
                                            .bodySmall
                                )

                                Spacer(
                                    modifier =
                                        Modifier.height(8.dp)
                                )

                                val bitmap = remember(item.rutaImagen) {

                                    try {

                                        item.rutaImagen?.let {
                                            ImageUtils.cargarBitmap(it)
                                        }

                                    } catch (e: Exception) {

                                        null
                                    }
                                }

                                if (bitmap != null) {

                                    Image(
                                        bitmap = bitmap.asImageBitmap(),
                                        contentDescription = "Transformación",
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(220.dp)
                                    )

                                } else {

                                    Text(
                                        text = "No se pudo cargar la imagen",
                                        color = MaterialTheme.colorScheme.error
                                    )
                                }

                                if (
                                    !item.leyenda.isNullOrBlank()
                                ) {

                                    Spacer(
                                        modifier =
                                            Modifier.height(8.dp)
                                    )

                                    HorizontalDivider()

                                    Spacer(
                                        modifier =
                                            Modifier.height(8.dp)
                                    )

                                    Text(
                                        text = "Leyenda",

                                        style =
                                            MaterialTheme
                                                .typography
                                                .labelLarge
                                    )

                                    Text(
                                        text =
                                            item.leyenda ?: "",

                                        style =
                                            MaterialTheme
                                                .typography
                                                .bodyMedium
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}