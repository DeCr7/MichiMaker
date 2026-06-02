package ni.edu.uam.michimaker.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ni.edu.uam.michimaker.utils.ImageUtils
import ni.edu.uam.michimaker.utils.TransformacionItem
import ni.edu.uam.michimaker.viewmodel.TransformacionViewModel

@Composable
fun HistoryScreen(
    navController: NavController,
    viewModel: TransformacionViewModel = viewModel()
) {

    val state by viewModel.uiState.collectAsState()

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Historial",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Total: ${state.total}",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (state.cargando) {

            CircularProgressIndicator()

        } else if (state.transformaciones.isEmpty()) {

            Text("No hay transformaciones aún")

        } else {

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                items(state.transformaciones) { item ->

                    TransformacionItem(
                        item = item,
                        onDelete = {
                            viewModel.eliminar(item)
                        }
                    )
                }
            }
        }
    }
}