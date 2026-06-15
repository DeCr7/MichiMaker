package ni.edu.uam.michimaker.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transformaciones")
data class TransformacionEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val nombreFiltro: String,

    val fecha: String,

    val rutaImagen: String
    
)