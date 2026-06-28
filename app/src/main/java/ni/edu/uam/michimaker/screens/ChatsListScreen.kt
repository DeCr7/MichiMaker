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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ni.edu.uam.michimaker.navigation.Routes
import ni.edu.uam.michimaker.utils.SessionManager
import ni.edu.uam.michimaker.viewmodel.ChatsListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatsListScreen(navController: NavController) {
    val usuarioLogueado by SessionManager.usuarioActual.collectAsState()
    val miId = usuarioLogueado?.id ?: 0

    if (miId == 0) {
        LaunchedEffect(Unit) {
            navController.navigate(Routes.LOGIN) {
                popUpTo(0)
            }
        }
        return
    }

    val viewModel: ChatsListViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ChatsListViewModel(miId) as T
            }
        }
    )

    val rawChats by viewModel.chats.collectAsState()

    val chatsValidos = remember(rawChats) {
        rawChats.filter {
            it.usuarioId > 0 &&
                    it.username != "MichiAmigo" &&
                    it.username != "Usuario Eliminado"
        }
    }

    val isRefreshing by viewModel.isRefreshing.collectAsState()

    val darkTheme = remember { SessionManager.esModoOscuroActivo() }

    val gradientColors = if (darkTheme) {
        listOf(Color(0xFF2C1B2E), Color(0xFF1F2235))
    } else {
        listOf(Color(0xFFFFC1E3), Color(0xFFC5CAE9))
    }
    val gradient = Brush.verticalGradient(colors = gradientColors)
    val textColor = if (darkTheme) Color.White else Color(0xFF2C1B2E)

    LaunchedEffect(Unit) {
        viewModel.cargarBandeja()
    }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = { Text("Mensajes 🐾", fontWeight = FontWeight.Bold, color = textColor) },
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient)
                .padding(paddingValues)
        ) {
            if (chatsValidos.isEmpty() && !isRefreshing) {
                Text(
                    text = "No tienes conversaciones activas.",
                    modifier = Modifier.align(Alignment.Center),
                    color = if (darkTheme) Color.LightGray else Color.Gray
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(chatsValidos) { chat ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    val destino = Routes.CHAT
                                        .replace("{otroUsuarioId}", chat.usuarioId.toString())
                                        .replace("{otroUsername}", chat.username ?: "Anonimo")
                                    navController.navigate(destino)
                                },
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (darkTheme) Color(0xFF252A34).copy(alpha = 0.85f) else Color.White.copy(alpha = 0.85f)
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val avatarBitmap = remember(chat.fotoPerfil) {
                                    try {
                                        if (!chat.fotoPerfil.isNullOrBlank()) {
                                            val cleanBase64 = if (chat.fotoPerfil.contains(",")) {
                                                chat.fotoPerfil.substring(chat.fotoPerfil.indexOf(",") + 1)
                                            } else chat.fotoPerfil
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
                                        contentDescription = "Avatar de ${chat.username}",
                                        modifier = Modifier
                                            .size(52.dp)
                                            .clip(CircleShape),
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.AccountCircle,
                                        contentDescription = "Avatar por defecto",
                                        modifier = Modifier.size(52.dp).clip(CircleShape),
                                        tint = if (darkTheme) Color.LightGray else Color.Gray
                                    )
                                }

                                Spacer(modifier = Modifier.width(16.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = chat.username ?: "Usuario de MichiMaker",
                                        fontWeight = FontWeight.Bold,
                                        color = textColor
                                    )
                                    Text(
                                        text = chat.ultimoMensaje,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = if (darkTheme) Color.LightGray else Color.DarkGray,
                                        maxLines = 1
                                    )
                                }

                                val visualFecha = chat.fechaEnvio.split(" ").lastOrNull() ?: ""
                                Text(
                                    text = visualFecha,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }
            }

            if (isRefreshing) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = if (darkTheme) Color(0xFFE1BEE7) else Color(0xFFBA68C8)
                )
            }
        }
    }
}