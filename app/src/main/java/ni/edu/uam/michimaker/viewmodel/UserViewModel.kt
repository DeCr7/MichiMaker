package ni.edu.uam.michimaker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ni.edu.uam.michimaker.dto.UsuarioDto
import ni.edu.uam.michimaker.repository.UsuarioRepository
import ni.edu.uam.michimaker.utils.SessionManager

class UserViewModel : ViewModel() {

    private val repository =
        UsuarioRepository()

    // =========================
    // USUARIO ACTUAL
    // =========================

    private val _usuario =
        MutableStateFlow<UsuarioDto?>(null)

    val usuario:
            StateFlow<UsuarioDto?> =
        _usuario

    // =========================
    // LOGIN
    // =========================

    private val _loginExitoso =
        MutableStateFlow(false)

    val loginExitoso:
            StateFlow<Boolean> =
        _loginExitoso

    // =========================
    // REGISTRO
    // =========================

    private val _registroExitoso =
        MutableStateFlow(false)

    val registroExitoso:
            StateFlow<Boolean> =
        _registroExitoso

    // =========================
    // LOADING
    // =========================

    private val _loading =
        MutableStateFlow(false)

    val loading:
            StateFlow<Boolean> =
        _loading

    // =========================
    // MENSAJES
    // =========================

    private val _mensaje =
        MutableStateFlow("")

    val mensaje:
            StateFlow<String> =
        _mensaje

    // =========================
    // LOGIN
    // =========================

    fun login(
        username: String,
        password: String
    ) {

        viewModelScope.launch {

            _loading.value = true

            try {

                val usuario =
                    repository.login(
                        username,
                        password
                    )

                if (usuario != null) {

                    SessionManager.login(usuario)

                    _usuario.value = usuario

                    _loginExitoso.value = true

                    _mensaje.value =
                        "Inicio de sesión exitoso"

                } else {

                    _loginExitoso.value = false

                    _mensaje.value =
                        "Usuario o contraseña incorrectos"
                }

            } catch (e: Exception) {

                e.printStackTrace()

                _loginExitoso.value = false

                _mensaje.value =
                    "Error al iniciar sesión"

            } finally {

                _loading.value = false
            }
        }
    }

    // =========================
    // REGISTRO
    // =========================

    fun registrar(
        usuario: UsuarioDto
    ) {

        viewModelScope.launch {

            _loading.value = true

            try {

                val exito =
                    repository.registrar(
                        usuario
                    )

                _registroExitoso.value =
                    exito

                _mensaje.value =
                    if (exito)
                        "Cuenta creada correctamente"
                    else
                        "No se pudo crear la cuenta"

            } catch (e: Exception) {

                _mensaje.value =
                    "Error durante el registro"

            } finally {

                _loading.value = false
            }
        }
    }

    // =========================
    // RESET
    // =========================

    fun limpiarEstados() {

        _loginExitoso.value = false
        _registroExitoso.value = false
        _mensaje.value = ""
    }

    fun cerrarSesion() {

        SessionManager.logout()

        _usuario.value = null

        _loginExitoso.value = false
    }
}