package ni.edu.uam.michimaker.screens

import android.graphics.BitmapFactory
import android.util.Base64
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ni.edu.uam.michimaker.dto.TransformacionFeedDto
import ni.edu.uam.michimaker.dto.UsuarioDto
import ni.edu.uam.michimaker.navigation.Routes
import ni.edu.uam.michimaker.repository.TransformacionRepository
import ni.edu.uam.michimaker.repository.UsuarioRepository
import ni.edu.uam.michimaker.utils.SessionManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    usuarioId: Int? = null
) {
    val context = LocalContext.current
    val usuarioLogueado by SessionManager.usuarioActual.collectAsState()

    // 🔥 CORRECCIÓN: Si es null o es -1 (nuestro default en NavHost), es el perfil propio
    val esMiPerfil = usuarioId == null || usuarioId == -1 || usuarioId == usuarioLogueado?.id
    val idAConsultar = if (esMiPerfil) (usuarioLogueado?.id ?: 0) else (usuarioId ?: 0)

    val transformacionRepo = remember { TransformacionRepository() }
    val usuarioRepo = remember { UsuarioRepository() }

    val profileViewModel: ProfileViewModel = viewModel(
        factory = ProfileViewModelFactory(transformacionRepo, usuarioRepo, idAConsultar, esMiPerfil, usuarioLogueado)
    )

    val usuarioTarget by profileViewModel.datosUsuario.collectAsState()
    val publicaciones by profileViewModel.postsUsuario.collectAsState()

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
            TopAppBar(
                title = {
                    Text(
                        text = if (esMiPerfil) "Mi Perfil" else "@${usuarioTarget?.username ?: "Perfil"}",
                        fontWeight = FontWeight.Bold,
                        color = if (darkTheme) Color.White else Color(0xFF2C1B2E)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Regresar",
                            tint = if (darkTheme) Color.White else Color(0xFF2C1B2E)
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
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // CABECERA DEL PERFIL
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (darkTheme) Color(0xFF252A34).copy(alpha = 0.85f) else Color.White.copy(alpha = 0.8f)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val avatarBitmap = remember(usuarioTarget?.fotoPerfil) {
                            try {
                                if (!usuarioTarget?.fotoPerfil.isNullOrBlank()) {
                                    val decodedString = Base64.decode(usuarioTarget?.fotoPerfil, Base64.DEFAULT)
                                    BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                                } else null
                            } catch (e: Exception) { null }
                        }

                        if (avatarBitmap != null) {
                            Image(
                                bitmap = avatarBitmap.asImageBitmap(),
                                contentDescription = "FotoPerfil",
                                modifier = Modifier
                                    .size(90.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = "FotoPerfil",
                                modifier = Modifier
                                    .size(90.dp)
                                    .clip(CircleShape),
                                tint = if (darkTheme) Color.LightGray else Color.Gray
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = usuarioTarget?.nombre ?: "Usuario de MichiMaker",
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            color = if (darkTheme) Color.White else Color(0xFF2C1B2E),
                            textAlign = TextAlign.Center
                        )

                        Text(
                            text = "@${usuarioTarget?.username ?: "anonimo"}",
                            fontSize = 14.sp,
                            color = if (darkTheme) Color.LightGray else Color(0xFF5C535E),
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = usuarioTarget?.biografia ?: "¡Este michi aún no ha escrito una biografía! 🐾",
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 16.dp),
                            color = if (darkTheme) Color.White else Color.Black
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        if (esMiPerfil) {
                            Button(
                                onClick = { navController.navigate(Routes.EDIT_PROFILE) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (darkTheme) Color(0xFF9C27B0) else Color(0xFFBA68C8),
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(50)
                            ) {
                                Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Editar Perfil", fontWeight = FontWeight.Bold)
                            }
                        } else {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
                            ) {
                                OutlinedButton(
                                    onClick = {
                                        val targetId = usuarioTarget?.id ?: 0
                                        val targetUsername = usuarioTarget?.username ?: "anonimo"

                                        // 🔥 SEGURO ANTI-FALLOS: Validamos que no apunte al ID 0 o al fallback temporal
                                        if (targetId > 0 && targetUsername != "MichiAmigo") {
                                            navController.navigate("chat/$targetId/$targetUsername")
                                        } else {
                                            Toast.makeText(context, "No se puede iniciar chat con este usuario", Toast.LENGTH_SHORT).show()
                                        }
                                    },
                                    shape = RoundedCornerShape(50),
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = if (darkTheme) Color(0xFFE1BEE7) else Color(0xFF6A1B9A)
                                    )
                                ) {
                                    Text("Mensaje 💬", maxLines = 1, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }

                Text(
                    text = "Transformaciones (${publicaciones.size})",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    color = if (darkTheme) Color.White else Color(0xFF2C1B2E)
                )

                if (publicaciones.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "Ninguna transformación publicada todavía 😿",
                            color = if (darkTheme) Color.LightGray else Color(0xFF5C535E),
                            fontWeight = FontWeight.Medium
                        )
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(publicaciones) { post ->
                            ProfileGridItem(post = post, darkTheme = darkTheme)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileGridItem(post: TransformacionFeedDto, darkTheme: Boolean) {
    val bitmap = remember(post.imagenBase64) {
        try {
            if (!post.imagenBase64.isNullOrBlank()) {
                val decodedString = Base64.decode(post.imagenBase64, Base64.DEFAULT)
                BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
            } else null
        } catch (e: Exception) { null }
    }

    Card(
        modifier = Modifier
            .aspectRatio(1f)
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        if (bitmap != null) {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(if (darkTheme) Color(0xFF37474F) else Color(0xFFE0E0E0))
            )
        }
    }
}

// ==========================================
// VIEWMODEL Y FACTORY INTEGRADOS
// ==========================================

class ProfileViewModel(
    private val transformacionRepo: TransformacionRepository,
    private val usuarioRepo: UsuarioRepository,
    private val usuarioId: Int,
    private val esMiPerfil: Boolean,
    private val miUsuario: UsuarioDto?
) : ViewModel() {

    private val _datosUsuario = MutableStateFlow<UsuarioDto?>(null)
    val datosUsuario: StateFlow<UsuarioDto?> = _datosUsuario

    private val _postsUsuario = MutableStateFlow<List<TransformacionFeedDto>>(emptyList())
    val postsUsuario: StateFlow<List<TransformacionFeedDto>> = _postsUsuario

    init {
        cargarDatos()
    }

    private fun cargarDatos() {
        viewModelScope.launch {
            if (esMiPerfil) {
                _datosUsuario.value = miUsuario
                if (usuarioId > 0) {
                    _postsUsuario.value = transformacionRepo.obtenerPorUsuario(usuarioId)
                }
            } else {
                val usuarioBuscado = usuarioRepo.obtenerPorId(usuarioId)
                if (usuarioBuscado != null) {
                    _datosUsuario.value = usuarioBuscado
                } else {
                    _datosUsuario.value = UsuarioDto(
                        id = usuarioId,
                        username = "MichiAmigo",
                        nombre = "MichiAmigo",
                        biografia = "¡Este michi aún no ha escrito una biografía! 🐾",
                        correo = "",
                        password = null,
                        fotoPerfil = null
                    )
                }
                if (usuarioId > 0) {
                    _postsUsuario.value = transformacionRepo.obtenerPorUsuario(usuarioId)
                }
            }
        }
    }
}

class ProfileViewModelFactory(
    private val transformacionRepo: TransformacionRepository,
    private val usuarioRepo: UsuarioRepository,
    private val usuarioId: Int,
    private val esMiPerfil: Boolean,
    private val miUsuario: UsuarioDto?
) : androidx.lifecycle.ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProfileViewModel(transformacionRepo, usuarioRepo, usuarioId, esMiPerfil, miUsuario) as T
    }
}