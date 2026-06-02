package ni.edu.uam.michimaker.utils

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.material3.*
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp

@Composable
fun TransformacionItem(
    item: ni.edu.uam.michimaker.database.TransformacionEntity,
    onDelete: () -> Unit
) {

    val bitmap = remember(item.rutaImagen) {
        ImageUtils.cargarBitmap(item.rutaImagen)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
    ) {

        Column(
            modifier = Modifier.padding(12.dp)
        ) {

            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = "Filtro: ${item.nombreFiltro}")
            Text(text = "Fecha: ${item.fecha}")

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onDelete
            ) {
                Text("Eliminar")
            }
        }
    }
}