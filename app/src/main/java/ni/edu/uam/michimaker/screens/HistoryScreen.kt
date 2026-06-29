package ni.edu.uam.michimaker.screens

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ni.edu.uam.michimaker.navigation.Routes
import ni.edu.uam.michimaker.repository.TransformacionRepository
import ni.edu.uam.michimaker.utils.SessionManager
import ni.edu.uam.michimaker.viewmodel.TransformacionViewModel
import ni.edu.uam.michimaker.viewmodel.TransformacionViewModelFactory

private fun bitmapDesdeBase64(base64: String?): Bitmap? {
    return try {
        if (base64.isNullOrBlank()) return null
        val bytes = Base64.decode(base64, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    } catch (e: Exception) {
        null
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    navController: NavController
) {
    // 🔥 CORRECCIÓN CRÍTICA: Cambiar la detección del sistema por la preferencia real en SessionManager
    val isDarkMode = remember { SessionManager.esModoOscuroActivo() }

    // Modificar el degradado de fondo (Pasteles Claros vs Tonos Noche)
    val gradient = Brush.verticalGradient(
        colors = if (isDarkMode) {
            listOf(
                Color(0xFF1E1E24), // Gris carbón muy oscuro
                Color(0xFF2D1B36), // Ciruela oscuro místico
                Color(0xFF1A2332)  // Marino oscuro base
            )
        } else {
            listOf(
                Color(0xFFF8BBD0), // Rosa viejo original
                Color(0xFFD1C4E9), // Lavanda original
                Color(0xFFFFECB3)  // Crema original
            )
        }
    )

    val usuario = SessionManager.obtenerUsuario()

    if (usuario == null) {
        LaunchedEffect(Unit) {
            navController.navigate(Routes.LOGIN) {
                popUpTo(0)
            }
        }
        return
    }

    val repository = remember { TransformacionRepository() }
    val viewModel: TransformacionViewModel = viewModel(
        factory = TransformacionViewModelFactory(
            repository = repository,
            usuarioId = usuario.id ?: 0
        )
    )

    val state by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Mi Historial",
                        fontWeight = FontWeight.Bold,
                        color = if (isDarkMode) Color.White else Color(0xFF2D1B36)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = if (isDarkMode) Color(0xFF1E1E24).copy(alpha = 0.85f) else Color.White.copy(alpha = 0.85f)
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient)
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                text = "Historial de ${usuario.username}",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = if (isDarkMode) Color.White else Color(0xFF2D1B36)
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = "Transformaciones guardadas en tu cuenta",
                color = if (isDarkMode) Color.LightGray else Color(0xFF5C535E)
            )

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    navController.navigate(Routes.HOME) {
                        launchSingleTop = true
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isDarkMode) Color(0xFF9C27B0) else Color(0xFFBA68C8),
                    contentColor = Color.White
                )
            ) {
                Text("Volver al inicio", fontWeight = FontWeight.Bold)
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Total de transformaciones: ${state.total}",
                fontWeight = FontWeight.Medium,
                color = if (isDarkMode) Color.LightGray else Color(0xFF2D1B36)
            )

            Spacer(Modifier.height(16.dp))

            when {
                state.cargando -> {
                    CircularProgressIndicator(
                        color = if (isDarkMode) Color(0xFFE1BEE7) else MaterialTheme.colorScheme.primary
                    )
                }

                state.error != null -> {
                    Text(
                        text = "Error: ${state.error}",
                        color = MaterialTheme.colorScheme.error
                    )
                }

                state.transformaciones.isEmpty() -> {
                    Text(
                        text = "No hay transformaciones registradas.",
                        color = if (isDarkMode) Color.White else Color(0xFF2D1B36)
                    )

                    Spacer(Modifier.height(12.dp))

                    Button(
                        onClick = { navController.navigate(Routes.CAMERA) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isDarkMode) Color(0xFF9C27B0) else Color(0xFFBA68C8)
                        )
                    ) {
                        Text("Crear una Transformación", fontWeight = FontWeight.Bold)
                    }
                }

                else -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = state.transformaciones,
                            key = { it.id }
                        ) { item ->

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isDarkMode) Color(0xFF252A34) else Color.White.copy(alpha = 0.7f)
                                )
                            ) {
                                Column(Modifier.padding(12.dp)) {
                                    Text(
                                        text = item.nombreFiltro,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isDarkMode) Color.White else Color(0xFF2D1B36)
                                    )

                                    Spacer(Modifier.height(6.dp))

                                    Text(
                                        text = "Fecha: ${item.fecha}",
                                        color = if (isDarkMode) Color.LightGray else Color(0xFF5C535E)
                                    )

                                    Spacer(Modifier.height(8.dp))

                                    val bitmap = remember(item.id, item.imagenBase64) {
                                        bitmapDesdeBase64(item.imagenBase64)
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

                                    if (!item.leyenda.isNullOrBlank()) {
                                        Spacer(Modifier.height(8.dp))
                                        HorizontalDivider(
                                            color = if (isDarkMode) Color.DarkGray else Color.LightGray
                                        )
                                        Spacer(Modifier.height(8.dp))

                                        Text(
                                            text = "Leyenda",
                                            style = MaterialTheme.typography.labelLarge,
                                            color = if (isDarkMode) Color.Gray else Color(0xFF5C535E)
                                        )

                                        Text(
                                            text = item.leyenda ?: "",
                                            color = if (isDarkMode) Color.White else Color.Black
                                        )

                                        Spacer(modifier = Modifier.height(12.dp))

                                        var editando by remember { mutableStateOf(false) }
                                        var nuevaLeyenda by remember { mutableStateOf(item.leyenda ?: "") }

                                        if (editando) {
                                            // Variable local para consistencia de color de texto
                                            val inputTextColor = if (isDarkMode) Color.White else Color(0xFF2C1B2E)

                                            OutlinedTextField(
                                                value = nuevaLeyenda,
                                                onValueChange = { nuevaLeyenda = it },
                                                modifier = Modifier.fillMaxWidth(),
                                                label = { Text("Editar leyenda") },
                                                colors = OutlinedTextFieldDefaults.colors(
                                                    focusedTextColor = inputTextColor, // 🌟 Corregido: Color del texto enfocado
                                                    unfocusedTextColor = inputTextColor, // 🌟 Corregido: Color del texto desenfocado
                                                    focusedBorderColor = if (isDarkMode) Color(0xFFE1BEE7) else Color(0xFF6A1B9A),
                                                    unfocusedBorderColor = if (isDarkMode) Color.Gray else Color(0xFF5C535E),
                                                    focusedLabelColor = if (isDarkMode) Color(0xFFE1BEE7) else Color(0xFF6A1B9A),
                                                    unfocusedLabelColor = if (isDarkMode) Color.Gray else Color(0xFF5C535E)
                                                )
                                            )

                                            Spacer(modifier = Modifier.height(8.dp))

                                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                                Button(
                                                    onClick = {
                                                        viewModel.actualizarLeyenda(item.id, nuevaLeyenda)
                                                        editando = false
                                                    },
                                                    colors = ButtonDefaults.buttonColors(
                                                        containerColor = if (isDarkMode) Color(0xFF9C27B0) else Color(0xFFBA68C8)
                                                    )
                                                ) {
                                                    Text("Guardar", fontWeight = FontWeight.Bold)
                                                }

                                                OutlinedButton(
                                                    onClick = {
                                                        editando = false
                                                        nuevaLeyenda = item.leyenda ?: ""
                                                    }
                                                ) {
                                                    Text("Cancelar")
                                                }
                                            }
                                        } else {
                                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                                Button(
                                                    onClick = { editando = true },
                                                    colors = ButtonDefaults.buttonColors(
                                                        containerColor = if (isDarkMode) Color(0xFF9C27B0) else Color(0xFFBA68C8)
                                                    )
                                                ) {
                                                    Text("Editar")
                                                }

                                                Button(
                                                    onClick = { viewModel.eliminarTransformacion(item.id) },
                                                    colors = ButtonDefaults.buttonColors(
                                                        containerColor = MaterialTheme.colorScheme.error
                                                    )
                                                ) {
                                                    Text("Eliminar")
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}