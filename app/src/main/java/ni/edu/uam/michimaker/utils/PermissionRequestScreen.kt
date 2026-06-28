package ni.edu.uam.michimaker.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun PermissionRequestScreen(
    onRequest: () -> Unit
) {
    val darkTheme = isSystemInDarkTheme()

    // 🔥 MODIFICADO: Degradado místico adaptativo consistente con el flujo de la cámara/resultado
    val gradientColors = if (darkTheme) {
        listOf(
            Color(0xFF152238), // Azul noche profundo
            Color(0xFF2D1B33), // Violeta oscuro
            Color(0xFF2C1A24)  // Vino oscuro
        )
    } else {
        listOf(
            Color(0xFFB3E5FC), // Azul cielo
            Color(0xFFE1BEE7), // Violeta
            Color(0xFFFCE4EC)  // Rosa claro
        )
    }
    val gradient = Brush.verticalGradient(colors = gradientColors)

    val accentColor = if (darkTheme) Color(0xFFE1BEE7) else Color(0xFFBA68C8)
    val textColor = if (darkTheme) Color.White else Color(0xFF2C1B2E)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Permiso de cámara requerido 🐾",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = textColor
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "MichiMaker necesita acceder a tu cámara para poder aplicar los filtros en tiempo real.",
            style = MaterialTheme.typography.bodyMedium,
            color = if (darkTheme) Color.LightGray else Color(0xFF5C535E),
            modifier = Modifier.padding(horizontal = 16.dp),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onRequest,
            colors = ButtonDefaults.buttonColors(
                containerColor = accentColor,
                contentColor = Color.White
            )
        ) {
            Text(
                text = "Conceder permiso",
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}
