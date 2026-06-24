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
import ni.edu.uam.michimaker.utils.SessionManager
import ni.edu.uam.michimaker.viewmodel.ResultViewModel

@Composable
fun ResultScreen(
    navController: NavController,
    image: String,
    filter: String,
    viewModel: ResultViewModel = viewModel()
) {

    val gradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFB3E5FC), // azul cielo
            Color(0xFFE1BEE7), // violeta
            Color(0xFFFCE4EC)  // rosa claro
        )
    )

    val usuario by remember {
        mutableStateOf(
            SessionManager.obtenerUsuario()
        )
    }

    var leyenda by remember {
        mutableStateOf("")
    }

    val state by viewModel.state.collectAsState()

    LaunchedEffect(image, filter) {

        viewModel.procesarImagen(
            image,
            filter
        )
    }

    val bitmap = remember(
        state.resultadoImagen,
        image
    ) {

        state.resultadoImagen?.let {

            ImageUtils.cargarBitmap(it)

        } ?: ImageUtils.cargarBitmap(image)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
            .padding(16.dp),

        horizontalAlignment =
            Alignment.CenterHorizontally
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

                Button(
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
                    Text("Volver al Inicio")
                }
            }

            bitmap != null -> {

                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Resultado",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp)
                )

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                Text(
                    text = "Filtro aplicado: $filter",
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                OutlinedTextField(
                    value = leyenda,

                    onValueChange = {
                        leyenda = it
                    },

                    modifier = Modifier.fillMaxWidth(),

                    label = {
                        Text("Leyenda")
                    },

                    placeholder = {
                        Text(
                            "Describe tu transformación..."
                        )
                    },

                    singleLine = false,

                    minLines = 3,

                    colors = OutlinedTextFieldDefaults.colors(

                        focusedLabelColor =
                            Color.Black,

                        unfocusedLabelColor =
                            Color.Black,


                        focusedTextColor =
                            Color.Black,

                        unfocusedTextColor =
                            Color.Black,


                        focusedPlaceholderColor =
                            Color.DarkGray,

                        unfocusedPlaceholderColor =
                            Color.Gray,


                        focusedBorderColor =
                            Color(0xFFBA68C8),

                        unfocusedBorderColor =
                            Color.Gray,


                        cursorColor =
                            Color(0xFFBA68C8)
                    )
                )

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                Button(
                    modifier = Modifier.fillMaxWidth(),

                    enabled =
                        state.resultadoImagen != null &&
                                !state.guardando,

                    onClick = {

                        val rutaResultado =
                            state.resultadoImagen
                                ?: return@Button

                        android.util.Log.d(
                            "RESULT",
                            "Usuario actual: $usuario"
                        )

                        android.util.Log.d(
                            "RESULT",
                            "Usuario ID enviado: ${usuario?.id ?: 0}"
                        )

                        viewModel.guardarTransformacion(

                            filtro = filter,
                            rutaImagen = rutaResultado,
                            usuarioId = usuario?.id ?: 0,
                            leyenda = leyenda

                        ) {

                            navController.navigate(
                                Routes.HISTORY
                            )
                        }
                    }
                ) {
                    if(state.guardando) {

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            CircularProgressIndicator(
                                modifier =
                                    Modifier.size(20.dp)
                            )

                            Spacer(
                                Modifier.width(8.dp)
                            )

                            Text(
                                "Guardando..."
                            )
                        }

                    } else {

                        Text(
                            "Guardar Transformación"
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Button(
                    modifier = Modifier.fillMaxWidth(),

                    onClick = {

                        navController.navigate(
                            Routes.HOME
                        ) {
                            popUpTo(Routes.HOME)
                        }
                    }
                ) {
                    Text("Descartar")
                }

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Button(
                    modifier = Modifier.fillMaxWidth(),

                    onClick = {

                        navController.navigate(
                            Routes.HISTORY
                        )
                    }
                ) {
                    Text("Ver Historial")
                }
            }

            else -> {

                Text(
                    text = "No se pudo cargar la imagen"
                )

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                Button(
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
                    Text("Volver al Inicio")
                }
            }
        }
    }
}