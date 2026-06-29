package ni.edu.uam.michimaker.screens

import android.graphics.BitmapFactory
import android.util.Base64
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ni.edu.uam.michimaker.dto.MensajeDto
import ni.edu.uam.michimaker.navigation.Routes
import ni.edu.uam.michimaker.utils.SessionManager
import ni.edu.uam.michimaker.viewmodel.ChatViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    navController: NavController,
    otroUsuarioId: Int,
    otroUsername: String,
    viewModel: ChatViewModel = viewModel()
) {
    val context = LocalContext.current
    val usuarioLogueado by SessionManager.usuarioActual.collectAsState()
    val miId = usuarioLogueado?.id ?: 0

    // Seguridad de ruta interna
    if (otroUsuarioId <= 0) {
        LaunchedEffect(Unit) {
            Toast.makeText(context, "ID de usuario inválido", Toast.LENGTH_SHORT).show()
            navController.popBackStack()
        }
        return
    }

    val mensajes by viewModel.mensajes.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val nombreOtroUsuario by viewModel.nombreOtroUsuario.collectAsState()
    val fotoPerfilBase64 by viewModel.fotoPerfilOtroUsuario.collectAsState()
    val fotoBitmap = recuerdaBitmapBase64(fotoPerfilBase64)

    var textoMensaje by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    LaunchedEffect(otroUsuarioId) {
        viewModel.iniciarChat(remitenteId = miId, receptorId = otroUsuarioId)
    }

    LaunchedEffect(mensajes.size) {
        if (mensajes.isNotEmpty()) {
            listState.animateScrollToItem(mensajes.size - 1)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.detenerPolling()
        }
    }

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
                    // 👆 Se agrega el modificador .clickable a la fila superior completa
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                val destinoPerfil = Routes.PROFILE.replace("{usuarioId}", otroUsuarioId.toString())
                                navController.navigate(destinoPerfil)
                            }
                            .padding(vertical = 4.dp)
                    ) {
                        if (fotoBitmap != null) {
                            Image(
                                bitmap = fotoBitmap.asImageBitmap(),
                                contentDescription = "Foto de perfil",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = "Sin foto de perfil",
                                modifier = Modifier.size(38.dp),
                                tint = if (darkTheme) Color.LightGray else Color.Gray
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column {
                            Text(
                                text = if (nombreOtroUsuario.isNullOrBlank() || nombreOtroUsuario == "Cargando..." || nombreOtroUsuario == "MichiAmigo") {
                                    "@$otroUsername"
                                } else {
                                    "@$nombreOtroUsuario"
                                },
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = if (darkTheme) Color.White else Color(0xFF2C1B2E)
                            )
                            Text(
                                text = "Chat de Michis 🐾",
                                fontSize = 12.sp,
                                color = if (darkTheme) MaterialTheme.colorScheme.onSurfaceVariant else Color(0xFF5C535E)
                            )
                        }
                    }
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
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = if (darkTheme) Color(0xFFE1BEE7) else MaterialTheme.colorScheme.primary
                )
            } else {
                Column(modifier = Modifier.fillMaxSize()) {

                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(mensajes) { mensaje ->
                            val esMio = mensaje.remitenteId == miId
                            BurbujaChat(mensaje = mensaje, esMio = esMio, darkTheme = darkTheme)
                        }
                    }

                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        tonalElevation = 8.dp,
                        color = if (darkTheme) Color(0xFF1F2235).copy(alpha = 0.95f) else Color.White.copy(alpha = 0.95f)
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextField(
                                value = textoMensaje,
                                onValueChange = { textoMensaje = it },
                                placeholder = { Text("Escribe un miau-ensaje...", color = if (darkTheme) Color.LightGray else Color.Gray) },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(24.dp),
                                colors = TextFieldDefaults.colors(
                                    focusedTextColor = if (darkTheme) Color.White else Color.Black,
                                    unfocusedTextColor = if (darkTheme) Color.White else Color.Black,
                                    focusedContainerColor = if (darkTheme) Color(0xFF2C1B2E) else Color(0xFFF5F5F5),
                                    unfocusedContainerColor = if (darkTheme) Color(0xFF2C1B2E) else Color(0xFFF5F5F5),
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent
                                )
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            IconButton(
                                onClick = {
                                    if (textoMensaje.isNotBlank()) {
                                        viewModel.enviarMensaje(textoMensaje)
                                        textoMensaje = ""
                                    }
                                },
                                colors = IconButtonDefaults.iconButtonColors(
                                    containerColor = if (darkTheme) Color(0xFF9C27B0) else Color(0xFFBA68C8),
                                    contentColor = Color.White
                                )
                            ) {
                                Icon(Icons.Default.Send, contentDescription = "Enviar")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BurbujaChat(mensaje: MensajeDto, esMio: Boolean, darkTheme: Boolean) {
    val alineacion = if (esMio) Alignment.CenterEnd else Alignment.CenterStart

    val colorBurbuja = if (esMio) {
        if (darkTheme) Color(0xFF6A1B9A) else Color(0xFFE1BEE7)
    } else {
        if (darkTheme) Color(0xFF37474F) else Color(0xFFE0E0E0)
    }

    val colorTexto = if (esMio) {
        if (darkTheme) Color.White else Color(0xFF4A148C)
    } else {
        if (darkTheme) Color.White else Color(0xFF212121)
    }

    val formaBurbuja = if (esMio) {
        RoundedCornerShape(16.dp, 16.dp, 0.dp, 16.dp)
    } else {
        RoundedCornerShape(16.dp, 16.dp, 16.dp, 0.dp)
    }

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = alineacion
    ) {
        Surface(
            color = colorBurbuja,
            shape = formaBurbuja,
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                Text(
                    text = mensaje.contenido,
                    color = colorTexto,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Composable
private fun recuerdaBitmapBase64(base64String: String?): android.graphics.Bitmap? {
    return remember(base64String) {
        if (!base64String.isNullOrBlank()) {
            try {
                val cleanedString = base64String.substringAfter(",")
                val byteArray = Base64.decode(cleanedString, Base64.DEFAULT)
                BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        } else {
            null
        }
    }
}