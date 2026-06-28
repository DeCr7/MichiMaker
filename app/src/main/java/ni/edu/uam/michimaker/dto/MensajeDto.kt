package ni.edu.uam.michimaker.dto

data class MensajeDto(
    val id: Long? = null,
    val remitenteId: Int,
    val receptorId: Int,
    val contenido: String,
    val fechaEnvio: String? = null,
    val leido: Boolean = false
)