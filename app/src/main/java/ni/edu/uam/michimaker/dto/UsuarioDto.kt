package ni.edu.uam.michimaker.dto

data class UsuarioDto(

    val id: Int? = null,

    val username: String,

    val nombre: String,

    val correo: String,

    val password: String? = null,

    val fotoPerfil: String? = null
)