package ni.edu.uam.michimaker.screens

import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import ni.edu.uam.michimaker.dto.UsuarioDto
import ni.edu.uam.michimaker.repository.UsuarioRepository
import ni.edu.uam.michimaker.utils.ImageUtils
import ni.edu.uam.michimaker.utils.SessionManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(navController: NavController) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val userRepo = remember { UsuarioRepository() }

    // Obtenemos los datos actuales de la sesión de forma reactiva
    val usuarioLogueado by SessionManager.usuarioActual.collectAsState()

    // Estados mutables para la edición
    var nombre by remember { mutableStateOf("") }
    var biografia by remember { mutableStateOf("") }
    var fotoBase64 by remember { mutableStateOf<String?>(null) }
    var isSubmitting by remember { mutableStateOf(false) }

    // Sincronizar estados locales una vez que los datos del SessionManager estén disponibles
    LaunchedEffect(usuarioLogueado) {
        usuarioLogueado?.let { user ->
            if (nombre.isBlank()) nombre = user.nombre
            if (biografia.isBlank()) biografia = user.biografia ?: ""
            if (fotoBase64 == null) fotoBase64 = user.fotoPerfil
        }
    }

    // Launcher para abrir la galería del dispositivo
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val base64Result = ImageUtils.uriToBase64(context, it)
            if (base64Result != null) {
                fotoBase64 = base64Result
            } else {
                Toast.makeText(context, "Error al procesar la imagen", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // 🔥 CORRECCIÓN CRÍTICA: Leer la preferencia real guardada en SessionManager
    val darkTheme = remember { SessionManager.esModoOscuroActivo() }

    val gradientColors = if (darkTheme) {
        listOf(Color(0xFF2C1B2E), Color(0xFF251A15), Color(0xFF1F2235))
    } else {
        listOf(Color(0xFFFFC1E3), Color(0xFFFFE0B2), Color(0xFFC5CAE9))
    }
    val gradient = Brush.verticalGradient(colors = gradientColors)

    // 🎨 Color adaptativo del texto interno para asegurar contraste en modo claro y oscuro
    val inputTextColor = if (darkTheme) Color.White else Color(0xFF2C1B2E)

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Editar Perfil",
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
                    containerColor = if (darkTheme) Color(0xFF1F2235).copy(alpha = 0.85f) else Color.White.copy(alpha = 0.85f)
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
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // SECCIÓN DE LA FOTO (Avatar interactivo)
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clickable { galleryLauncher.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    val bitmap = remember(fotoBase64) {
                        try {
                            if (!fotoBase64.isNullOrBlank()) {
                                val decodedString = Base64.decode(fotoBase64, Base64.DEFAULT)
                                BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                            } else null
                        } catch (e: Exception) { null }
                    }

                    if (bitmap != null) {
                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = "Foto de perfil",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(if (darkTheme) Color(0xFF37474F) else Color(0xFFE0E0E0), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("🐾", fontSize = 40.sp)
                        }
                    }

                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(if (darkTheme) Color(0xFF9C27B0) else Color(0xFFBA68C8), CircleShape)
                            .align(Alignment.BottomEnd),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "Cambiar Foto",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Toca para cambiar la foto",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (darkTheme) Color.LightGray else Color(0xFF5C535E)
                )

                Spacer(modifier = Modifier.height(32.dp))

                // CAMPO: NOMBRE
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre Completo") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = inputTextColor,       // 🔥 Corrección color letra activo
                        unfocusedTextColor = inputTextColor,     // 🔥 Corrección color letra inactivo
                        focusedBorderColor = if (darkTheme) Color(0xFFE1BEE7) else Color(0xFF6A1B9A),
                        unfocusedBorderColor = if (darkTheme) Color.Gray else Color(0xFF5C535E),
                        focusedLabelColor = if (darkTheme) Color(0xFFE1BEE7) else Color(0xFF6A1B9A),
                        unfocusedLabelColor = if (darkTheme) Color.Gray else Color(0xFF5C535E)
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // CAMPO BIOGRAFÍA (Multilínea)
                OutlinedTextField(
                    value = biografia,
                    onValueChange = { biografia = it },
                    label = { Text("Biografía Michi") },
                    placeholder = { Text("Cuéntale a la comunidad sobre ti y tus michis...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    shape = RoundedCornerShape(12.dp),
                    maxLines = 4,
                    singleLine = false,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = inputTextColor,       // 🔥 Corrección color letra activo
                        unfocusedTextColor = inputTextColor,     // 🔥 Corrección color letra inactivo
                        focusedPlaceholderColor = inputTextColor.copy(alpha = 0.6f), // 🔥 Corrección placeholder activo
                        unfocusedPlaceholderColor = inputTextColor.copy(alpha = 0.5f), // 🔥 Corrección placeholder inactivo
                        focusedBorderColor = if (darkTheme) Color(0xFFE1BEE7) else Color(0xFF6A1B9A),
                        unfocusedBorderColor = if (darkTheme) Color.Gray else Color(0xFF5C535E),
                        focusedLabelColor = if (darkTheme) Color(0xFFE1BEE7) else Color(0xFF6A1B9A),
                        unfocusedLabelColor = if (darkTheme) Color.Gray else Color(0xFF5C535E)
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                // BOTÓN GUARDAR
                Button(
                    onClick = {
                        val usuarioInfo = usuarioLogueado
                        if (usuarioInfo != null && nombre.isNotBlank()) {
                            isSubmitting = true
                            coroutineScope.launch {
                                val dtoActualizado = usuarioInfo.copy(
                                    nombre = nombre,
                                    fotoPerfil = fotoBase64,
                                    biografia = biografia
                                )

                                Log.d("MICHIMAKER_EDIT", "=== DETECTANDO DATOS PRE-ENVÍO ===")
                                Log.d("MICHIMAKER_EDIT", "Nombre capturado: $nombre")
                                Log.d("MICHIMAKER_EDIT", "Bio capturada: $biografia")
                                Log.d("MICHIMAKER_EDIT", "Longitud Base64 Foto: ${fotoBase64?.length ?: 0}")

                                val exito = userRepo.actualizarPerfil(usuarioInfo.id ?: 0, dtoActualizado)

                                if (exito) {
                                    SessionManager.guardarUsuario(dtoActualizado)
                                    isSubmitting = false
                                    Toast.makeText(context, "¡Perfil actualizado con éxito! ✨", Toast.LENGTH_SHORT).show()
                                    navController.popBackStack()
                                } else {
                                    isSubmitting = false
                                    Toast.makeText(context, "Error al guardar en el servidor ❌", Toast.LENGTH_SHORT).show()
                                }
                            }
                        } else {
                            Toast.makeText(context, "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    enabled = !isSubmitting,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (darkTheme) Color(0xFF9C27B0) else Color(0xFFBA68C8),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    if (isSubmitting) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    } else {
                        Text("Guardar Cambios", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}