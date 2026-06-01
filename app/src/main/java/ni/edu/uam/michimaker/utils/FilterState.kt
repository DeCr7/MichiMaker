package ni.edu.uam.michimaker.utils

data class FilterState(

    val procesando: Boolean = false,

    val imagenOriginal: String? = null,

    val imagenTransformada: String? = null,

    val filtroAplicado: String? = null,

    val error: String? = null
)