package ni.edu.uam.michimaker.utils

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun StatItem(
    filtro: String,
    cantidad: Int
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement =
                Arrangement.SpaceBetween
        ) {

            Text(
                text = filtro,
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = cantidad.toString(),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}