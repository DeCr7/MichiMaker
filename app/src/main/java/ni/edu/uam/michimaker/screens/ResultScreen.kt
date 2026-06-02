package ni.edu.uam.michimaker.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
                    Text(
                        "Tomar otra foto"
                    )
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
                    Text(
                        "Volver al inicio"
                    )
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
                    Text(
                        "Ver historial"
                    )
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
                    Text(
                        "Volver al inicio"
                    )
                }
            }
        }
    }
}