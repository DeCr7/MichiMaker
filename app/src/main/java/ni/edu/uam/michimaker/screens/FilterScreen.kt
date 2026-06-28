package ni.edu.uam.michimaker.screens

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ni.edu.uam.michimaker.utils.FilterCatalog
import ni.edu.uam.michimaker.utils.FilterItem
import ni.edu.uam.michimaker.utils.ImageUtils
import ni.edu.uam.michimaker.utils.SessionManager
import ni.edu.uam.michimaker.viewmodel.FilterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterScreen(
    navController: NavController,
    imagePath: String,
    filterViewModel: FilterViewModel = viewModel()
) {
    // 🔥 CORRECCIÓN CRÍTICA: Leer la preferencia real de la App (SessionManager)
    // en lugar de la configuración global del sistema de Android.
    val isDarkMode = remember { SessionManager.esModoOscuroActivo() }

    // El degradado cambiará correctamente ahora que 'isDarkMode' lee el SessionManager
    val gradient = Brush.verticalGradient(
        colors = if (isDarkMode) {
            listOf(
                Color(0xFF0F172A), // Azul pizarra muy oscuro
                Color(0xFF3B0764), // Púrpura místico oscuro
                Color(0xFF1E1B4B)  // Índigo profundo nocturno
            )
        } else {
            listOf(
                Color(0xFFB2DFDB), // Menta original
                Color(0xFFD1C4E9), // Violeta original
                Color(0xFFFFF9C4)  // Amarillo original
            )
        }
    )

    val selectedFilter by filterViewModel.filtroSeleccionado.collectAsState()

    val imageBitmap = remember(imagePath) {
        if (imagePath.isNotBlank()) {
            ImageUtils.cargarBitmap(imagePath)
        } else {
            null
        }
    }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Filtros Miau",
                        fontWeight = FontWeight.Bold,
                        color = if (isDarkMode) Color.White else Color(0xFF1E1B4B)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Regresar",
                            tint = if (isDarkMode) Color.White else Color(0xFF1E1B4B)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = if (isDarkMode) Color(0xFF0F172A).copy(alpha = 0.85f) else Color.White.copy(alpha = 0.85f)
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient)
                .padding(paddingValues)
                .padding(16.dp)
        ) {

            imageBitmap?.let { bitmap ->
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Imagen capturada",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Selecciona un filtro",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = if (isDarkMode) Color.White else Color(0xFF2C1B2E)
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(FilterCatalog.filtros) { filtro ->
                    FilterItem(
                        filtro = filtro,
                        selected = filtro == selectedFilter,
                        onClick = {
                            filterViewModel.seleccionarFiltro(filtro)
                        }
                    )
                }
            }

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(top = 8.dp),
                enabled = selectedFilter != null,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isDarkMode) Color(0xFF9C27B0) else Color(0xFFBA68C8),
                    contentColor = Color.White
                ),
                onClick = {
                    val filtro = selectedFilter ?: return@Button
                    navController.navigate(
                        "result/${Uri.encode(imagePath)}/${Uri.encode(filtro.nombre)}"
                    )
                }
            ) {
                Text("Aplicar filtro", fontWeight = FontWeight.Bold)
            }
        }
    }
}