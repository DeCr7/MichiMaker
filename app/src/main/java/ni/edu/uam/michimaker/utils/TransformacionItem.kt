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
import ni.edu.uam.michimaker.dto.TransformacionDto

@Composable
fun TransformacionItem(
    item: TransformacionDto
) {
    val bitmap = remember(item.rutaImagen) {

        item.rutaImagen?.let {
            ImageUtils.cargarBitmap(it)
        }
    }

    Column {

        bitmap?.let {

            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            )
        }

        Text("Filtro: ${item.nombreFiltro}")
        Text("Fecha: ${item.fecha}")

        item.leyenda?.let { leyenda ->

            if (leyenda.isNotBlank()) {
                Text("Leyenda: $leyenda")
            }
        }
    }
}