package ni.edu.uam.michimaker.utils

import ni.edu.uam.michimaker.dto.TransformacionDto

data class TransformacionUiState(

    val cargando: Boolean = false,

    val transformaciones:
    List<TransformacionDto> =
        emptyList(),

    val total: Int = 0,

    val error: String? = null
)