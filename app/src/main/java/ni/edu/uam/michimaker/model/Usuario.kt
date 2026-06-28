// Usuario.kt
package ni.edu.uam.michimaker.model

data class Usuario(
    val id: Int,
    val username: String,
    val nombre: String,
    val biografia: String? = null, // <- Nuevo campo
    val correo: String,
    val fotoPerfil: String? = null
)