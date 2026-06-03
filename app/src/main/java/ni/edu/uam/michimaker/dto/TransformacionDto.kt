package ni.edu.uam.michimaker.dto

data class TransformacionDto(
    val id: Int? = null,
    val nombreFiltro: String,
    val fecha: String,
    val rutaImagen: String
)