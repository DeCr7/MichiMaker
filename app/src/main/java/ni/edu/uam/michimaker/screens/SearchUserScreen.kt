package ni.edu.uam.michimaker.screens

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ni.edu.uam.michimaker.navigation.Routes
import ni.edu.uam.michimaker.utils.SessionManager
import ni.edu.uam.michimaker.viewmodel.SearchUserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchUserScreen(navController: NavController) {
    val viewModel: SearchUserViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    val darkTheme = remember { SessionManager.esModoOscuroActivo() }

    // Colores dinámicos idénticos a ChatsListScreen
    val gradientColors = if (darkTheme) {
        listOf(Color(0xFF2C1B2E), Color(0xFF1F2235))
    } else {
        listOf(Color(0xFFFFC1E3), Color(0xFFC5CAE9))
    }
    val gradient = Brush.verticalGradient(colors = gradientColors)
    val textColor = if (darkTheme) Color.White else Color(0xFF2C1B2E)

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = { Text("Buscar Amigos 🐾", fontWeight = FontWeight.Bold, color = textColor) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = textColor
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = if (darkTheme) Color(0xFF2C1B2E).copy(alpha = 0.85f) else Color.White.copy(alpha = 0.85f)
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient)
                .padding(paddingValues)
        ) {
            // Barra de búsqueda estilizada adaptada al tema
            OutlinedTextField(
                value = uiState.query,
                onValueChange = { viewModel.onQueryChanged(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = { Text("Escribe un nombre o username...", color = if (darkTheme) Color.LightGray else Color.Gray) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Buscar",
                        tint = if (darkTheme) Color.LightGray else Color.Gray
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = if (darkTheme) Color(0xFF252A34).copy(alpha = 0.6f) else Color.White.copy(alpha = 0.6f),
                    unfocusedContainerColor = if (darkTheme) Color(0xFF252A34).copy(alpha = 0.4f) else Color.White.copy(alpha = 0.4f),
                    focusedBorderColor = if (darkTheme) Color(0xFFE1BEE7) else Color(0xFFBA68C8),
                    unfocusedBorderColor = Color.Transparent,
                    focusedTextColor = textColor,
                    unfocusedTextColor = textColor
                )
            )

            // Contenedor principal de resultados
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = if (darkTheme) Color(0xFFE1BEE7) else Color(0xFFBA68C8)
                    )
                } else if (!uiState.error.isNullOrEmpty()) {
                    Text(
                        text = uiState.error ?: "Ocurrió un error inesperado",
                        modifier = Modifier.align(Alignment.Center).padding(16.dp),
                        color = Color.Red
                    )
                } else if (uiState.usuariosEncontrados.isEmpty()) {
                    val textoInformativo = if (uiState.query.trim().length < 3) {
                        "Escribe al menos 3 letras para empezar a buscar."
                    } else {
                        "No se encontraron michi-amigos."
                    }
                    Text(
                        text = textoInformativo,
                        modifier = Modifier.align(Alignment.Center),
                        color = if (darkTheme) Color.LightGray else Color.Gray
                    )
                } else {
                    // Lista idéntica a la estructura de ChatsListScreen
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(uiState.usuariosEncontrados) { usuario ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        // Navega directamente al chat usando el esquema de rutas existente
                                        val destino = Routes.CHAT
                                            .replace("{otroUsuarioId}", usuario.id.toString())
                                            .replace("{otroUsername}", usuario.username)
                                        navController.navigate(destino)
                                    },
                                shape = RoundedCornerShape(16.dp),
                                colors = CardColors(
                                    containerColor = if (darkTheme) Color(0xFF252A34).copy(alpha = 0.85f) else Color.White.copy(alpha = 0.85f),
                                    contentColor = textColor,
                                    disabledContainerColor = Color.Transparent,
                                    disabledContentColor = Color.Transparent
                                )
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Procesamiento idéntico de Avatares Base64
                                    val avatarBitmap = remember(usuario.fotoPerfil) {
                                        try {
                                            if (!usuario.fotoPerfil.isNullOrBlank()) {
                                                val cleanBase64 = if (usuario.fotoPerfil.contains(",")) {
                                                    usuario.fotoPerfil.substring(usuario.fotoPerfil.indexOf(",") + 1)
                                                } else usuario.fotoPerfil
                                                val decodedBytes = Base64.decode(cleanBase64, Base64.DEFAULT)
                                                BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
                                            } else null
                                        } catch (e: Exception) {
                                            null
                                        }
                                    }

                                    if (avatarBitmap != null) {
                                        Image(
                                            bitmap = avatarBitmap.asImageBitmap(),
                                            contentDescription = "Avatar de ${usuario.username}",
                                            modifier = Modifier
                                                .size(52.dp)
                                                .clip(CircleShape),
                                            contentScale = ContentScale.Crop
                                        )
                                    } else {
                                        Icon(
                                            imageVector = Icons.Default.AccountCircle,
                                            contentDescription = "Avatar por defecto",
                                            modifier = Modifier
                                                .size(52.dp)
                                                .clip(CircleShape),
                                            tint = if (darkTheme) Color.LightGray else Color.Gray
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(16.dp))

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = usuario.nombre,
                                            fontWeight = FontWeight.Bold,
                                            color = textColor
                                        )
                                        Text(
                                            text = "@${usuario.username}",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = if (darkTheme) Color.LightGray else Color.DarkGray,
                                            maxLines = 1
                                        )
                                        if (!usuario.biografia.isNullOrEmpty()) {
                                            Text(
                                                text = usuario.biografia,
                                                style = MaterialTheme.typography.bodySmall,
                                                color = Color.Gray,
                                                maxLines = 1
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
    }
}