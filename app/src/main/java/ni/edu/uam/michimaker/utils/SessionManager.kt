package ni.edu.uam.michimaker.utils

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ni.edu.uam.michimaker.dto.UsuarioDto

object SessionManager {

    private val _usuarioActual = MutableStateFlow<UsuarioDto?>(null)
    val usuarioActual: StateFlow<UsuarioDto?> = _usuarioActual

    // Instancia para SharedPreferences
    private var prefs: SharedPreferences? = null
    private const val KEY_DARK_MODE = "key_dark_mode"

    // Función crucial para inicializar el contexto desde fuera
    fun init(context: Context) {
        if (prefs == null) {
            prefs = context.getSharedPreferences("michimaker_prefs", Context.MODE_PRIVATE)
        }
    }

    // Agrega esta función dentro de tu SessionManager.java actual
    fun guardarUsuario(usuario: UsuarioDto) {
        _usuarioActual.value = usuario
    }

    fun login(usuario: UsuarioDto) {
        _usuarioActual.value = usuario
    }

    fun logout() {
        _usuarioActual.value = null
    }

    fun obtenerUsuario(): UsuarioDto? {
        return _usuarioActual.value
    }

    fun guardarModoOscuro(enabled: Boolean) {
        // Usamos la variable 'prefs' verificando que no sea nula de forma segura
        prefs?.edit()?.putBoolean(KEY_DARK_MODE, enabled)?.apply()
    }

    fun esModoOscuroActivo(): Boolean {
        // Retorna falso por defecto si no se ha guardado nada o si 'prefs' es nulo
        return prefs?.getBoolean(KEY_DARK_MODE, false) ?: false
    }
}