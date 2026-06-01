package ni.edu.uam.michimaker.model

import ni.edu.uam.michimaker.database.TransformacionEntity

data class Transformacion(

    val id: Int,
    val filtro: String,
    val fecha: String,
    val rutaImagen: String
)