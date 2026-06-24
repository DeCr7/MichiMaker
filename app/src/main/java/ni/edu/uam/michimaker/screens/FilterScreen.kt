package ni.edu.uam.michimaker.screens

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ni.edu.uam.michimaker.utils.FilterCatalog
import ni.edu.uam.michimaker.utils.FilterItem
import ni.edu.uam.michimaker.utils.ImageUtils
import ni.edu.uam.michimaker.viewmodel.FilterViewModel
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.asImageBitmap

@Composable
fun FilterScreen(
    navController: NavController,
    imagePath: String,
    filterViewModel: FilterViewModel = viewModel()
) {

    // Fondo degradado (mismo estilo MichiMaker)
    val gradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFB2DFDB), // menta
            Color(0xFFD1C4E9), // violeta
            Color(0xFFFFF9C4)  // amarillo
        )
    )

    val selectedFilter by filterViewModel
        .filtroSeleccionado
        .collectAsState()

    val imageBitmap = remember(imagePath) {
        if (imagePath.isNotBlank()) {
            ImageUtils.cargarBitmap(imagePath)
        } else {
            null
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient) // ✔ SOLO agregado aquí
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
            style = MaterialTheme.typography.titleMedium
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
                .padding(top = 8.dp),
            enabled = selectedFilter != null,
            onClick = {

                val filtro = selectedFilter ?: return@Button

                navController.navigate(
                    "result/${
                        Uri.encode(imagePath)
                    }/${
                        Uri.encode(filtro.nombre)
                    }"
                )
            }
        ) {
            Text("Aplicar filtro")
        }
    }
}