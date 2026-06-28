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
import androidx.compose.ui.text.font.FontWeight
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
    // 🔥 CORRECCIÓN CRÍTICA: Reemplazar la lectura del sistema por la de SessionManager
    val darkTheme = remember { SessionManager.esModoOscuroActivo() }

    // Gradiente adaptativo para no romper la estética mística/felina en modo oscuro
    val gradientColors = if (darkTheme) {
        listOf(
            Color(0xFF152238), // Azul noche profundo
            Color(0xFF2D1B33), // Violeta oscuro
            Color(0xFF2C1A24)  // Rosa viejo / vino oscuro
        )
    } else {
        listOf(
            Color(0xFFB3E5FC), // azul cielo
            Color(0xFFE1BEE7), // violeta
            Color(0xFFFCE4EC)  // rosa claro
        )
    }
    val gradient = Brush.verticalGradient(colors = gradientColors)

    val usuario by remember {
        mutableStateOf(SessionManager.obtenerUsuario())
    }

    var leyenda by remember {
        mutableStateOf("")
    }

    val state by viewModel.state.collectAsState()

    LaunchedEffect(image, filter) {
        viewModel.procesarImagen(image, filter)
    }

    val bitmap = remember(state.resultadoImagen, image) {
        state.resultadoImagen?.let {
            ImageUtils.cargarBitmap(it)
        } ?: ImageUtils.cargarBitmap(image)
    }

    // Definición de color base para textos libres según el fondo
    val textColor = if (darkTheme) Color.White else Color(0xFF2C1B2E)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Resultado",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = textColor
        )

        Spacer(modifier = Modifier.height(16.dp))

        when {
            state.loading -> {
                CircularProgressIndicator(color = if (darkTheme) Color(0xFFE1BEE7) else Color(0xFF6A1B9A))
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Aplicando filtro...",
                    fontWeight = FontWeight.Medium,
                    color = textColor
                )
            }

            state.error != null -> {
                Text(
                    text = "Error: ${state.error}",
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        navController.navigate(Routes.CAMERA) {
                            popUpTo(Routes.HOME)
                        }
                    }
                ) {
                    Text("Tomar otra foto")
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        navController.navigate(Routes.HOME) {
                            popUpTo(Routes.HOME) { inclusive = true }
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

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Filtro aplicado: $filter",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )

                Spacer(modifier = Modifier.height(16.dp))

                val customAccentColor = if (darkTheme) Color(0xFFE1BEE7) else Color(0xFFBA68C8)

                OutlinedTextField(
                    value = leyenda,
                    onValueChange = { leyenda = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Leyenda") },
                    placeholder = { Text("Describe tu transformación...") },
                    singleLine = false,
                    minLines = 3,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = textColor,            // 🔥 Asegura legibilidad al escribir activo
                        unfocusedTextColor = textColor,          // 🔥 Asegura legibilidad al escribir inactivo
                        focusedPlaceholderColor = textColor.copy(alpha = 0.6f), // 🔥 Contraste para el texto de sugerencia activo
                        unfocusedPlaceholderColor = textColor.copy(alpha = 0.5f), // 🔥 Contraste para el texto de sugerencia inactivo
                        focusedLabelColor = customAccentColor,
                        unfocusedLabelColor = if (darkTheme) Color.LightGray else Color.Gray,
                        focusedBorderColor = customAccentColor,
                        unfocusedBorderColor = if (darkTheme) Color.Gray else Color(0xFF5C535E)
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    enabled = state.resultadoImagen != null && !state.guardando,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = customAccentColor,
                        contentColor = Color.White
                    ),
                    onClick = {
                        val rutaResultado = state.resultadoImagen ?: return@Button
                        viewModel.guardarTransformacion(
                            filtro = filter,
                            rutaImagen = rutaResultado,
                            usuarioId = usuario?.id ?: 0,
                            leyenda = leyenda
                        ) {
                            navController.navigate(Routes.HISTORY)
                        }
                    }
                ) {
                    if (state.guardando) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                            Spacer(Modifier.width(8.dp))
                            Text("Guardando...", color = Color.White)
                        }
                    } else {
                        Text("Guardar Transformación", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        navController.navigate(Routes.HOME) {
                            popUpTo(Routes.HOME)
                        }
                    }
                ) {
                    Text("Descartar")
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        navController.navigate(Routes.HISTORY)
                    }
                ) {
                    Text("Ver Historial")
                }
            }
            else -> {
                Text(
                    text = "No se pudo cargar la imagen",
                    color = textColor,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        navController.navigate(Routes.HOME) {
                            popUpTo(Routes.HOME) { inclusive = true }
                        }
                    }
                ) {
                    Text("Volver al Inicio")
                }
            }
        }
    }
}