package ni.edu.uam.michimaker.model

data class FaceDetectionState(

    val cargando: Boolean = false,

    val rostrosDetectados: Int = 0,

    val rostroEncontrado: Boolean = false,

    val mensajeError: String? = null
)