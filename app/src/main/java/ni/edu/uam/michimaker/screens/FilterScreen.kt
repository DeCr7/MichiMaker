package ni.edu.uam.michimaker.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ni.edu.uam.michimaker.navigation.Routes
import ni.edu.uam.michimaker.utils.FilterCatalog
import ni.edu.uam.michimaker.utils.FilterItem
import ni.edu.uam.michimaker.utils.ImageUtils
import ni.edu.uam.michimaker.viewmodel.CameraViewModel
import ni.edu.uam.michimaker.viewmodel.FilterViewModel

@Composable
fun FilterScreen(
    navController: NavController,
    cameraViewModel: CameraViewModel = viewModel(),
    filterViewModel: FilterViewModel = viewModel()
) {

    val context = LocalContext.current

    val cameraState by cameraViewModel.cameraState.collectAsState()
    val selectedFilter by filterViewModel.filtroSeleccionado.collectAsState()

    val imageBitmap = remember(cameraState.imagePath) {
        cameraState.imagePath?.let {
            ImageUtils.cargarBitmap(it)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        // ---------------- IMAGE PREVIEW ----------------
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

        // ---------------- FILTER LIST ----------------
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

        // ---------------- ACTION BUTTON ----------------
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            enabled = selectedFilter != null,
            onClick = {

                val path = cameraState.imagePath ?: return@Button
                val filtro = selectedFilter ?: return@Button

                navController.navigate(
                    "result/${path}/${filtro.nombre}"
                )
            }
        ) {
            Text("Aplicar filtro")
        }
    }
}