package ni.edu.uam.michimaker.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ni.edu.uam.michimaker.database.AppDatabaseProvider
import ni.edu.uam.michimaker.repository.TransformacionRepository
import ni.edu.uam.michimaker.utils.TransformacionItem
import ni.edu.uam.michimaker.viewmodel.TransformacionViewModel

@Composable
fun HistoryScreen(
    navController: NavController
) {

    val context = LocalContext.current

    val repository = remember {
        TransformacionRepository(
            AppDatabaseProvider
                .obtener(context)
                .transformacionDao()
        )
    }

    val viewModel = remember {
        TransformacionViewModel(repository)
    }

    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Historial",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = "Total: ${state.total}",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        when {

            state.cargando -> {

                CircularProgressIndicator()
            }

            state.transformaciones.isEmpty() -> {

                Text(
                    text = "No hay transformaciones aún"
                )
            }

            else -> {

                LazyColumn(
                    verticalArrangement =
                        Arrangement.spacedBy(12.dp)
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
}