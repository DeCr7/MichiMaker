package ni.edu.uam.michimaker.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ni.edu.uam.michimaker.R
import ni.edu.uam.michimaker.navigation.Routes
import ni.edu.uam.michimaker.utils.SessionManager

@Composable
fun HomeScreen(
    navController: NavController
) {

    val usuario =
        SessionManager.obtenerUsuario()

    if (usuario == null) {

        LaunchedEffect(Unit) {

            navController.navigate(
                Routes.LOGIN
            ) {
                popUpTo(0)
            }
        }

        return
    }

    val gradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFFFD1DC),
            Color(0xFFFFE0B2),
            Color(0xFFE1BEE7)
        )
    )

    val buttonModifier = Modifier
        .width(280.dp)
        .height(56.dp)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(
            modifier = Modifier.weight(1f)
        )

        Image(
            painter = painterResource(
                id = R.drawable.michimaker_logo
            ),
            contentDescription = "MichiMaker Logo",
            modifier = Modifier.size(120.dp)
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = "Bienvenido, ${usuario.nombre}",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(
            modifier = Modifier.height(4.dp)
        )

        Text(
            text = "@${usuario.username}",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Text(
            text = "Convierte tu rostro en una versión felina divertida 🐱",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Black
        )

        Spacer(
            modifier = Modifier.height(40.dp)
        )

        Button(
            onClick = {
                navController.navigate(
                    Routes.CAMERA
                )
            },
            modifier = buttonModifier
        ) {
            Text("Iniciar Cámara")
        }

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Button(
            onClick = {
                navController.navigate(
                    Routes.HISTORY
                )
            },
            modifier = buttonModifier
        ) {
            Text("Mi Historial")
        }

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Button(
            onClick = {
                navController.navigate(
                    Routes.STATS
                )
            },
            modifier = buttonModifier
        ) {
            Text("Estadísticas")
        }

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Button(
            onClick = {
                navController.navigate(
                    Routes.SETTINGS
                )
            },
            modifier = buttonModifier
        ) {
            Text("Configuración")
        }

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        OutlinedButton(
            onClick = {

                SessionManager.logout()

                navController.navigate(
                    Routes.LOGIN
                ) {
                    popUpTo(0)
                }
            },
            modifier = buttonModifier
        ) {
            Text("Cerrar Sesión")
        }

        Spacer(
            modifier = Modifier.weight(1f)
        )
    }
}