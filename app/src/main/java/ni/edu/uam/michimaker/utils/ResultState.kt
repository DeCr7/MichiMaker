package ni.edu.uam.michimaker.utils

data class ResultState(
    val loading: Boolean = false,
    val resultadoImagen: String? = null,
    val error: String? = null
)