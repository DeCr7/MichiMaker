package ni.edu.uam.michimaker.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ni.edu.uam.michimaker.navigation.Routes
import ni.edu.uam.michimaker.repository.TransformacionRepository
import ni.edu.uam.michimaker.utils.SessionManager
import ni.edu.uam.michimaker.utils.StatItem
import ni.edu.uam.michimaker.viewmodel.StatsScreenViewModel

@Composable
fun StatsScreen(
    navController: NavController
) {
    // 🔥 CORRECCIÓN CRÍTICA: Cambiar a var y usar un LaunchedEffect para forzar
    // la lectura fresca de los SharedPreferences cada vez que se entra/vuelve a esta pantalla.
    var darkTheme by remember { mutableStateOf(SessionManager.esModoOscuroActivo()) }

    LaunchedEffect(Unit) {
        darkTheme = SessionManager.esModoOscuroActivo()
    }

    // Transición de gradiente cálido a tonos oscuros confortables
    val gradientColors = if (darkTheme) {
        listOf(
            Color(0xFF2E1A1A), // Coral quemado oscuro
            Color(0xFF2A1F15), // Durazno oscuro apagado
            Color(0xFF242515)  // Amarillo oliva oscuro
        )
    } else {
        listOf(
            Color(0xFFFFCCBC), // coral suave
            Color(0xFFFFE0B2), // durazno
            Color(0xFFFFF9C4)  // amarillo
        )
    }
    val gradient = Brush.verticalGradient(colors = gradientColors)

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
    val viewModel = remember {
        StatsScreenViewModel(
            repository = repository,
            usuarioId = usuario.id ?: 0
        )
    }

    val state by viewModel.uiState.collectAsState()

    // Paleta de colores local optimizada para alta legibilidad sobre el degradado
    val accentColor = if (darkTheme) Color(0xFFE1BEE7) else Color(0xFFBA68C8)
    val textColor = if (darkTheme) Color.White else Color(0xFF2C1B2E)
    val subTextColor = if (darkTheme) Color.LightGray else Color(0xFF5C535E)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
            .padding(16.dp)
    ) {
        Button(
            onClick = {
                navController.navigate(Routes.HOME) {
                    launchSingleTop = true
                    popUpTo(Routes.HOME)
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = accentColor,
                contentColor = Color.White
            )
        ) {
            Text("Volver al inicio", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Estadísticas de ${usuario.username}",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = textColor
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = if (darkTheme) Color(0xFF252A34).copy(alpha = 0.85f) else Color.White.copy(alpha = 0.85f)
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Total de transformaciones",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (darkTheme) Color.LightGray else Color(0xFF5C535E)
                )
                Text(
                    text = state.total.toString(),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (darkTheme) Color.White else Color(0xFF2C1B2E)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Por filtro",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = textColor
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (state.porFiltro.isEmpty()) {
            Text(
                text = "No hay datos aún",
                style = MaterialTheme.typography.bodyMedium,
                color = subTextColor
            )
        } else {
            state.porFiltro.forEach { (filtro, cantidad) ->
                // 🔥 TIP EXCELENTE: Si los textos de StatItem se ven negros en modo oscuro,
                // asegúrate de pasarle una variable de color o el 'darkTheme' por parámetro.
                StatItem(
                    filtro = filtro,
                    cantidad = cantidad
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}