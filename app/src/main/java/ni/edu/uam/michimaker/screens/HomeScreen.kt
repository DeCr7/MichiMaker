package ni.edu.uam.michimaker.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ni.edu.uam.michimaker.R
import ni.edu.uam.michimaker.navigation.Routes

@Composable
fun HomeScreen(navController: NavController) {

    // Fondo degradado (tema gato: rosa, lila, naranja suave)
    val gradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFFFD1DC), // rosa gato
            Color(0xFFFFE0B2), // crema suave
            Color(0xFFE1BEE7) // lavanda
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // LOGO
        Image(
            painter = painterResource(id = R.drawable.cat_logo),
            contentDescription = "MichiMaker Logo",
            modifier = Modifier.size(120.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // NOMBRE APP
        Text(
            text = "MichiMaker",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(8.dp))

        // FRASE
        Text(
            text = "Convierte tu rostro en una versión felina divertida 🐱",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(32.dp))

        // BOTONES
        Button(onClick = {
            navController.navigate(Routes.CAMERA)
        }) {
            Text("Iniciar Cámara")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            navController.navigate(Routes.HISTORY)
        }) {
            Text("Historial")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            navController.navigate(Routes.STATS)
        }) {
            Text("Estadísticas")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            navController.navigate(Routes.SETTINGS)
        }) {
            Text("Configuración")
        }
    }
}