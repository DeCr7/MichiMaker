package ni.edu.uam.michimaker.dto

data class TransformacionFeedDto(

    val id: Int,

    val nombreFiltro: String,

    val fecha: String,

    val rutaImagen: String?,

    val usuarioId: Int,

    val username: String,

    val fotoPerfil: String?,

    val leyenda: String?,

    val imagenBase64: String?
)