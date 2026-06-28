package ni.edu.uam.michimaker.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ni.edu.uam.michimaker.navigation.Routes
import ni.edu.uam.michimaker.repository.TransformacionRepository
import ni.edu.uam.michimaker.utils.SessionManager
import ni.edu.uam.michimaker.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(
    navController: NavController
) {
    // 1. Obtener de manera reactiva el usuario de la sesión actual
    val usuario by SessionManager.usuarioActual.collectAsState()

    if (usuario == null) {
        LaunchedEffect(Unit) {
            navController.navigate(Routes.LOGIN) {
                popUpTo(0)
            }
        }
        return
    }

    val context = LocalContext.current
    val repository = remember { TransformacionRepository() }
    val viewModel = remember { SettingsViewModel(repository) }

    // 2. Estado local mutante enlazado directamente a los SharedPreferences del mánager
    var isDarkMode by remember {
        mutableStateOf(SessionManager.esModoOscuroActivo())
    }

    // 3. Degradado dinámico reactivo al estado local `isDarkMode`
    val gradient = Brush.verticalGradient(
        colors = if (isDarkMode) {
            listOf(
                Color(0xFF1A1A2E), // Azul noche muy oscuro
                Color(0xFF16213E), // Azul oscuro intermedio
                Color(0xFF0F3460)  // Azul profundo base
            )
        } else {
            listOf(
                Color(0xFFCFD8DC), // Gris azulado claro original
                Color(0xFFECEFF1),
                Color(0xFFD7CCC8)
            )
        }
    )

    // Paleta de colores adaptativa instantánea
    val accentColor = if (isDarkMode) Color(0xFFE1BEE7) else Color(0xFFBA68C8)
    val textColor = if (isDarkMode) Color.White else Color(0xFF2C1B2E)
    val subTextColor = if (isDarkMode) Color.LightGray else Color(0xFF5C535E)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
            .padding(16.dp)
    ) {
        Text(
            text = "Configuración",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = textColor
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Usuario: ${usuario?.username}",
            style = MaterialTheme.typography.bodyMedium,
            color = subTextColor
        )

        Spacer(modifier = Modifier.height(24.dp))

        // SECCIÓN TEMA OSCURO
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = if (isDarkMode) Color(0xFF252A34).copy(alpha = 0.85f) else Color.White.copy(alpha = 0.8f)
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Tema Oscuro Michi",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )
                    Text(
                        text = "Activa el diseño místico nocturno",
                        style = MaterialTheme.typography.bodySmall,
                        color = subTextColor
                    )
                }

                Switch(
                    checked = isDarkMode,
                    onCheckedChange = { checked ->
                        // Sincroniza Compose y SharedPreferences al mismo tiempo
                        isDarkMode = checked
                        SessionManager.guardarModoOscuro(checked)
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = accentColor,
                        checkedTrackColor = accentColor.copy(alpha = 0.5f)
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // SECCIÓN HISTORIAL
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = if (isDarkMode) Color(0xFF252A34).copy(alpha = 0.85f) else Color.White.copy(alpha = 0.8f)
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Historial",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        viewModel.limpiarHistorialUsuario(usuario?.id ?: 0) {
                            navController.navigate(Routes.HOME) {
                                launchSingleTop = true
                                popUpTo(Routes.HOME)
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFEF5350),
                        contentColor = Color.White
                    )
                ) {
                    Text("Limpiar mi historial", fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // SECCIÓN ACERCA DE
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = if (isDarkMode) Color(0xFF252A34).copy(alpha = 0.85f) else Color.White.copy(alpha = 0.8f)
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "MichiMaker",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Versión 1.1.3", style = MaterialTheme.typography.bodySmall, color = subTextColor)
                Text(text = "Filtros felinos con ML Kit", style = MaterialTheme.typography.bodySmall, color = subTextColor)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // BOTÓN: REGRESAR
        Button(
            onClick = {
                navController.navigate(Routes.HOME) {
                    launchSingleTop = true
                    popUpTo(Routes.HOME)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = accentColor,
                contentColor = Color.White
            )
        ) {
            Text("Volver al Inicio", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(12.dp))

        // BOTÓN: CERRAR SESIÓN
        OutlinedButton(
            onClick = {
                SessionManager.logout()
                navController.navigate(Routes.LOGIN) {
                    popUpTo(0)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = if (isDarkMode) Color(0xFFFF8A80) else Color(0xFFEF5350)
            )
        ) {
            Text("Cerrar Sesión", fontWeight = FontWeight.Bold)
        }
    }
}