package ni.edu.uam.michimaker.utils

import ni.edu.uam.michimaker.database.TransformacionEntity

data class TransformacionUiState(

    val cargando: Boolean = false,

    val transformaciones:
    List<TransformacionEntity> =
        emptyList(),

    val total: Int = 0,

    val error: String? = null
)