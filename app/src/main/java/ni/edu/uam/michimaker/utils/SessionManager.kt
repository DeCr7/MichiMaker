package ni.edu.uam.michimaker.utils

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ni.edu.uam.michimaker.dto.UsuarioDto

object SessionManager {

    private val _usuarioActual =
        MutableStateFlow<UsuarioDto?>(null)

    val usuarioActual:
            StateFlow<UsuarioDto?> =
        _usuarioActual

    fun login(
        usuario: UsuarioDto
    ) {
        _usuarioActual.value = usuario
    }

    fun logout() {
        _usuarioActual.value = null
    }

    fun obtenerUsuario():
            UsuarioDto? {

        return _usuarioActual.value
    }
}