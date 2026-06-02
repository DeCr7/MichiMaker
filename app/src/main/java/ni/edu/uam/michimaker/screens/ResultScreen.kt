package ni.edu.uam.michimaker.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ni.edu.uam.michimaker.navigation.Routes
import ni.edu.uam.michimaker.utils.ImageUtils
import ni.edu.uam.michimaker.viewmodel.ResultViewModel

@Composable
fun ResultScreen(
    navController: NavController,
    image: String,
    filter: String,
    viewModel: ResultViewModel = viewModel()
) {

    // Fondo degradado consistente con toda la app
    val gradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFBBDEFB), // azul claro
            Color(0xFFD1C4E9), // violeta suave
            Color(0xFFE3F2FD) // azul hielo
        )
    )

    val state by viewModel.state.collectAsState()

    val bitmap = remember(
        state.resultadoImagen,
        image
    ) {
        state.resultadoImagen?.let {
            ImageUtils.cargarBitmap(it)
        } ?: ImageUtils.cargarBitmap(image)
    }

    LaunchedEffect(image, filter) {
        viewModel.procesarImagen(
            image,
            filter
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient) // ✔ SOLO agregado aquí
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Resultado",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        when {

            state.loading -> {

                CircularProgressIndicator()

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                Text(
                    text = "Aplicando filtro..."
                )
            }

            state.error != null -> {

                Text(
                    text = "Error: ${state.error}",
                    color = MaterialTheme.colorScheme.error
                )

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                Button(
                    onClick = {

                        navController.navigate(
                            Routes.CAMERA
                        ) {
                            popUpTo(Routes.HOME)
                        }
                    }
                ) {
                    Text("Tomar otra foto")
                }

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                OutlinedButton(
                    onClick = {

                        navController.navigate(
                            Routes.HOME
                        ) {
                            popUpTo(Routes.HOME) {
                                inclusive = true
                            }
                        }
                    }
                ) {
                    Text("Volver al inicio")
                }
            }

            bitmap != null -> {

                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Resultado",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                )

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                Button(
                    onClick = {
                        navController.navigate(
                            Routes.HISTORY
                        )
                    }
                ) {
                    Text("Ver historial")
                }
            }

            else -> {

                Text(
                    text = "No se pudo cargar la imagen."
                )

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                OutlinedButton(
                    onClick = {

                        navController.navigate(
                            Routes.HOME
                        ) {
                            popUpTo(Routes.HOME) {
                                inclusive = true
                            }
                        }
                    }
                ) {
                    Text("Volver al inicio")
                }
            }
        }
    }
}