package ni.edu.uam.michimaker.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
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
import ni.edu.uam.michimaker.navigation.Routes
import ni.edu.uam.michimaker.utils.SessionManager
import ni.edu.uam.michimaker.viewmodel.UserViewModel

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: UserViewModel
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }

    val loginExitoso by viewModel.loginExitoso.collectAsStateWithLifecycle()
    val loading by viewModel.loading.collectAsStateWithLifecycle()
    val colorMensaje by viewModel.colorMensaje.collectAsStateWithLifecycle()
    val mensaje by viewModel.mensaje.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.limpiarEstados()
    }

    LaunchedEffect(loginExitoso) {
        if (loginExitoso) {
            navController.navigate(Routes.HOME) {
                popUpTo(Routes.LOGIN) { inclusive = true }
            }
        }
    }

    // 🔥 CORRECCIÓN CRÍTICA: Reemplazar la lectura del sistema por la de SessionManager
    val darkTheme = remember { SessionManager.esModoOscuroActivo() }

    val gradientColors = if (darkTheme) {
        listOf(
            Color(0xFF1A237E).copy(alpha = 0.4f), // Azul noche oscuro
            Color(0xFF4A148C).copy(alpha = 0.4f), // Lavanda oscuro
            Color(0xFF3E2723).copy(alpha = 0.3f)  // Crema/marrón oscuro
        )
    } else {
        listOf(
            Color(0xFFE1F5FE), // azul nube
            Color(0xFFF3E5F5), // lavanda clara
            Color(0xFFFFF3E0)  // crema
        )
    }
    val gradient = Brush.verticalGradient(colors = gradientColors)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier.size(240.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.michimaker_logo),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Tu red social de filtros con estilo salvaje",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = if (darkTheme) Color.LightGray else Color(0xFF2C1B2E)
            )
            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (darkTheme) Color(0xFF252A34).copy(alpha = 0.9f) else Color.White.copy(alpha = 0.85f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Iniciar Sesión",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = if (darkTheme) Color.White else Color(0xFF2C1B2E)
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    val accentColor = if (darkTheme) Color(0xFFE1BEE7) else Color(0xFFBA68C8)

                    // 🎨 Color común para los textos internos (Morado oscuro para modo claro, Blanco para modo oscuro)
                    val inputTextColor = if (darkTheme) Color.White else Color(0xFF2C1B2E)
                    val iconColor = if (darkTheme) Color.LightGray else Color(0xFF5C535E)

                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        label = { Text("Usuario") },
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = inputTextColor,       // 🔥 Fuerza el color del texto activo
                            unfocusedTextColor = inputTextColor,     // 🔥 Fuerza el color del texto inactivo
                            focusedLeadingIconColor = accentColor,
                            unfocusedLeadingIconColor = iconColor,
                            focusedBorderColor = accentColor,
                            unfocusedBorderColor = if (darkTheme) Color.Gray else Color(0xFF5C535E),
                            focusedLabelColor = accentColor,
                            unfocusedLabelColor = if (darkTheme) Color.LightGray else Color.Gray
                        )
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Contraseña") },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions.Default,
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = inputTextColor,       // 🔥 Fuerza el color de la contraseña activa
                            unfocusedTextColor = inputTextColor,     // 🔥 Fuerza el color de la contraseña inactiva
                            focusedLeadingIconColor = accentColor,
                            unfocusedLeadingIconColor = iconColor,
                            focusedBorderColor = accentColor,
                            unfocusedBorderColor = if (darkTheme) Color.Gray else Color(0xFF5C535E),
                            focusedLabelColor = accentColor,
                            unfocusedLabelColor = if (darkTheme) Color.LightGray else Color.Gray
                        )
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                    ) {
                        Checkbox(
                            checked = rememberMe,
                            onCheckedChange = { rememberMe = it },
                            colors = CheckboxDefaults.colors(checkedColor = accentColor)
                        )
                        Text(
                            text = "Recordarme en este dispositivo",
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (darkTheme) Color.LightGray else Color(0xFF5C535E)
                        )
                    }

                    if (mensaje.isNotBlank()) {
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = colorMensaje.copy(alpha = 0.15f)
                            )
                        ) {
                            Text(
                                text = mensaje,
                                modifier = Modifier.padding(10.dp),
                                color = if (darkTheme && colorMensaje == Color.Red) Color(0xFFFF8A80) else colorMensaje,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = { viewModel.login(username.trim(), password) },
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
                                strokeWidth = 2.dp,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Verificando...", color = Color.White)
                        } else {
                            Text("Entrar", fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    TextButton(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        onClick = { navController.navigate(Routes.REGISTER) }
                    ) {
                        Text(
                            text = "Crear una cuenta nueva",
                            fontWeight = FontWeight.Bold,
                            color = if (darkTheme) Color(0xFFB39DDB) else Color(0xFF7E57C2)
                        )
                    }
                }
            }
        }
    }
}