package ni.edu.uam.michimaker.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import ni.edu.uam.michimaker.R
import ni.edu.uam.michimaker.dto.UsuarioDto
import ni.edu.uam.michimaker.utils.SessionManager
import ni.edu.uam.michimaker.viewmodel.UserViewModel

@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: UserViewModel
) {
    var nombre by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var localError by remember { mutableStateOf<String?>(null) }

    val registroExitoso by viewModel.registroExitoso.collectAsStateWithLifecycle()
    val loading by viewModel.loading.collectAsStateWithLifecycle()
    val mensaje by viewModel.mensaje.collectAsStateWithLifecycle()
    val colorMensaje by viewModel.colorMensaje.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.limpiarEstados()
    }

    LaunchedEffect(registroExitoso) {
        if (registroExitoso) {
            navController.popBackStack()
        }
    }

    // 🔥 CORRECCIÓN CRÍTICA: Reemplazar la lectura del sistema por la de SessionManager
    val darkTheme = remember { SessionManager.esModoOscuroActivo() }

    val gradientColors = if (darkTheme) {
        listOf(
            Color(0xFF1A237E).copy(alpha = 0.4f),
            Color(0xFF4A148C).copy(alpha = 0.4f),
            Color(0xFF3E2723).copy(alpha = 0.3f)
        )
    } else {
        listOf(
            Color(0xFFE1F5FE), // azul nube
            Color(0xFFF3E5F5), // lavanda clara
            Color(0xFFFFF3E0)  // crema
        )
    }
    val gradient = Brush.verticalGradient(colors = gradientColors)
    val accentColor = if (darkTheme) Color(0xFFE1BEE7) else Color(0xFFBA68C8)

    // 🎨 Colores adaptativos para asegurar el contraste de la letra e iconos internos
    val inputTextColor = if (darkTheme) Color.White else Color(0xFF2C1B2E)
    val iconColor = if (darkTheme) Color.LightGray else Color(0xFF5C535E)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (darkTheme) Color(0xFF252A34).copy(alpha = 0.9f) else Color.White.copy(alpha = 0.85f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(modifier = Modifier.size(100.dp)) {
                        Image(
                            painter = painterResource(id = R.drawable.michimaker_logo),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Crea tu perfil",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = if (darkTheme) Color.White else Color(0xFF2C1B2E)
                    )
                    Text(
                        text = "Guarda tus fotos y filtros en un solo lugar",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (darkTheme) Color.LightGray else Color(0xFF5C535E)
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // 🛠️ Se agregaron las propiedades de color de texto y de íconos aquí
                    val standardTextFieldColors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = inputTextColor,
                        unfocusedTextColor = inputTextColor,
                        focusedLeadingIconColor = accentColor,
                        unfocusedLeadingIconColor = iconColor,
                        focusedLabelColor = accentColor,
                        unfocusedLabelColor = if (darkTheme) Color.LightGray else Color.Gray,
                        focusedBorderColor = accentColor,
                        unfocusedBorderColor = if (darkTheme) Color.Gray else Color(0xFF5C535E)
                    )

                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it; localError = null },
                        label = { Text("Nombre Completo") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = standardTextFieldColors
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it; localError = null },
                        label = { Text("Nombre de Usuario") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = standardTextFieldColors
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = correo,
                        onValueChange = { correo = it; localError = null },
                        label = { Text("Correo electrónico") },
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = standardTextFieldColors
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it; localError = null },
                        label = { Text("Contraseña") },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions.Default,
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = standardTextFieldColors
                    )

                    val errorAMostrar = localError ?: if (mensaje.isNotBlank() && colorMensaje == Color.Red) mensaje else null
                    errorAMostrar?.let {
                        Text(
                            text = it,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            if (nombre.isBlank() || username.isBlank() || correo.isBlank() || password.isBlank()) {
                                localError = "Por favor, completa todos los campos"
                            } else if (!correo.contains("@") || !correo.contains(".")) {
                                localError = "Escribe un correo válido"
                            } else if (password.length < 6) {
                                localError = "La contraseña debe tener mínimo 6 caracteres"
                            } else {
                                viewModel.registrar(
                                    UsuarioDto(
                                        username = username.trim(),
                                        nombre = nombre.trim(),
                                        correo = correo.trim(),
                                        password = password,
                                        fotoPerfil = null
                                    )
                                )
                            }
                        },
                        enabled = !loading,
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = accentColor,
                            contentColor = Color.White
                        )
                    ) {
                        if (loading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("Registrarme", fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    TextButton(onClick = { navController.popBackStack() }) {
                        Text(
                            text = "Ya tengo una cuenta",
                            fontWeight = FontWeight.Bold,
                            color = if (darkTheme) Color(0xFFB39DDB) else Color(0xFF7E57C2)
                        )
                    }
                }
            }
        }
    }
}