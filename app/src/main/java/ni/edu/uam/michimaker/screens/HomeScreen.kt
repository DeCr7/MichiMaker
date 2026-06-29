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
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ni.edu.uam.michimaker.dto.TransformacionFeedDto
import ni.edu.uam.michimaker.navigation.Routes
import ni.edu.uam.michimaker.utils.SessionManager
import ni.edu.uam.michimaker.viewmodel.TransformacionViewModel
import ni.edu.uam.michimaker.viewmodel.TransformacionViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController
) {
    val usuario by SessionManager.usuarioActual.collectAsState()

    if (usuario == null) {
        LaunchedEffect(Unit) {
            navController.navigate(Routes.LOGIN) {
                popUpTo(0)
            }
        }
        return
    }

    val appRepository = ni.edu.uam.michimaker.repository.TransformacionRepository()
    val viewModel: TransformacionViewModel = viewModel(
        factory = TransformacionViewModelFactory(appRepository, usuario?.id ?: 0)
    )

    val feedPosts by viewModel.feedState.collectAsState()
    val isRefreshing by viewModel.isRefreshingFeed.collectAsState()

    val darkTheme = remember { SessionManager.esModoOscuroActivo() }

    val gradientColors = if (darkTheme) {
        listOf(Color(0xFF2C1B2E), Color(0xFF251A15), Color(0xFF1F2235))
    } else {
        listOf(Color(0xFFFFC1E3), Color(0xFFFFE0B2), Color(0xFFC5CAE9))
    }
    val gradient = Brush.verticalGradient(colors = gradientColors)

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            var mostrarMenu by remember { mutableStateOf(false) }

            TopAppBar(
                title = {
                    Text(
                        text = "MichiMaker",
                        fontWeight = FontWeight.Bold,
                        fontSize = 19.sp, // Reducido ligeramente para dar más espacio
                        color = if (darkTheme) Color.White else Color(0xFF2C1B2E)
                    )
                },
                actions = {
                    // 🔍 ACCIÓN PRINCIPAL 1: Buscar
                    IconButton(onClick = { navController.navigate(Routes.SEARCH_USER) }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Buscar Amigos",
                            tint = if (darkTheme) Color(0xFFE1BEE7) else Color(0xFF6A1B9A)
                        )
                    }

                    // 💬 ACCIÓN PRINCIPAL 2: Chats
                    IconButton(onClick = { navController.navigate(Routes.CHATS_LIST) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Chat,
                            contentDescription = "Mensajes",
                            tint = if (darkTheme) Color(0xFFE1BEE7) else Color(0xFF6A1B9A)
                        )
                    }

                    // 📷 ACCIÓN PRINCIPAL 3: Cámara
                    IconButton(onClick = { navController.navigate(Routes.CAMERA) }) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "Cámara",
                            tint = if (darkTheme) Color(0xFFE1BEE7) else Color(0xFF6A1B9A)
                        )
                    }

                    // 👤 ACCIÓN PRINCIPAL 4: Perfil
                    IconButton(onClick = {
                        val destinoPerfil = Routes.PROFILE.replace("{usuarioId}", (usuario?.id ?: 0).toString())
                        navController.navigate(destinoPerfil)
                    }) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Mi Perfil",
                            tint = if (darkTheme) Color(0xFFB39DDB) else Color(0xFF673AB7)
                        )
                    }

                    // 📦 MENÚ DESPLEGABLE (Agrupa las acciones secundarias para liberar espacio)
                    Box {
                        IconButton(onClick = { mostrarMenu = true }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "Más opciones",
                                tint = if (darkTheme) Color.White else Color(0xFF2C1B2E)
                            )
                        }

                        DropdownMenu(
                            expanded = mostrarMenu,
                            onDismissRequest = { mostrarMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Historial") },
                                leadingIcon = { Icon(Icons.Default.History, contentDescription = null) },
                                onClick = {
                                    mostrarMenu = false
                                    navController.navigate(Routes.HISTORY)
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Estadísticas") },
                                leadingIcon = { Icon(Icons.Default.BarChart, contentDescription = null) },
                                onClick = {
                                    mostrarMenu = false
                                    navController.navigate(Routes.STATS)
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Configuración") },
                                leadingIcon = { Icon(Icons.Default.Settings, contentDescription = null) },
                                onClick = {
                                    mostrarMenu = false
                                    navController.navigate(Routes.SETTINGS)
                                }
                            )
                            Divider() // Una línea divisoria antes de cerrar sesión
                            DropdownMenuItem(
                                text = { Text("Salir", color = Color(0xFFEF5350)) },
                                leadingIcon = { Icon(Icons.Default.Logout, contentDescription = null, tint = Color(0xFFEF5350)) },
                                onClick = {
                                    mostrarMenu = false
                                    SessionManager.logout()
                                    navController.navigate(Routes.LOGIN) { popUpTo(0) }
                                }
                            )
                        }
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
            if (feedPosts.isEmpty() && !isRefreshing) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("🐱", fontSize = 64.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "¡El Feed está vacío!\nSé el primero en transformar tu rostro y compartirlo con el mundo.",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                        color = if (darkTheme) Color.White else Color(0xFF2C1B2E)
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = { navController.navigate(Routes.CAMERA) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (darkTheme) Color(0xFF9C27B0) else Color(0xFFBA68C8),
                            contentColor = Color.White
                        )
                    ) {
                        Text("Abrir Cámara", fontWeight = FontWeight.Bold)
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(12.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Text(
                            text = "Explorar Michis de la Comunidad ✨",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = if (darkTheme) Color.White else Color(0xFF2C1B2E),
                            modifier = Modifier.padding(vertical = 4.dp, horizontal = 4.dp)
                        )
                    }

                    items(feedPosts) { post ->
                        MichiPostCard(post = post, navController = navController, darkTheme = darkTheme)
                    }
                }
            }

            if (isRefreshing) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = if (darkTheme) Color(0xFFE1BEE7) else Color(0xFF6A1B9A)
                )
            }
        }
    }
}

@Composable
fun MichiPostCard(post: TransformacionFeedDto, navController: NavController, darkTheme: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (darkTheme) Color(0xFF252A34).copy(alpha = 0.85f) else Color.White.copy(alpha = 0.8f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        post.usuarioId?.let { id ->
                            val destinoPerfilAutor = Routes.PROFILE.replace("{usuarioId}", id.toString())
                            navController.navigate(destinoPerfilAutor)
                        }
                    }
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val avatarBitmap = remember(post.fotoPerfil) {
                    try {
                        if (!post.fotoPerfil.isNullOrBlank()) {
                            val cleanBase64 = if (post.fotoPerfil.contains(",")) {
                                post.fotoPerfil.substring(post.fotoPerfil.indexOf(",") + 1)
                            } else post.fotoPerfil
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
                        contentDescription = "Avatar de ${post.username}",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Avatar por defecto",
                        modifier = Modifier.size(40.dp).clip(CircleShape),
                        tint = if (darkTheme) Color.LightGray else Color.Gray
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = post.username ?: "Usuario Anónimo",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (darkTheme) Color.White else Color(0xFF2C1B2E)
                    )
                    Text(
                        text = "Filtro: ${post.nombreFiltro} • ${post.fecha}",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (darkTheme) Color.LightGray else Color(0xFF5C535E)
                    )
                }
            }

            val bitmap = remember(post.imagenBase64) {
                try {
                    if (!post.imagenBase64.isNullOrBlank()) {
                        val decodedString = Base64.decode(post.imagenBase64, Base64.DEFAULT)
                        BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                    } else null
                } catch (e: Exception) {
                    null
                }
            }

            if (bitmap != null) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Michi Filtro",
                    modifier = Modifier.fillMaxWidth().height(300.dp),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(if (darkTheme) Color(0xFF37474F) else Color(0xFFE0E0E0)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Imagen no disponible", color = if (darkTheme) Color.LightGray else Color.Gray)
                }
            }

            if (!post.leyenda.isNullOrBlank()) {
                Column(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
                    Text(
                        text = post.leyenda,
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (darkTheme) Color.White else Color.Black
                    )
                }
            }
        }
    }
}