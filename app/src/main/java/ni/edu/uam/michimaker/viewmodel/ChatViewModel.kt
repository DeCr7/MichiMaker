package ni.edu.uam.michimaker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ni.edu.uam.michimaker.dto.MensajeDto
import ni.edu.uam.michimaker.repository.ChatRepository

class ChatViewModel : ViewModel() {

    private val repository = ChatRepository()

    // Estado reactivo que contiene la lista de mensajes de la conversación actual
    private val _mensajes = MutableStateFlow<List<MensajeDto>>(emptyList())
    val mensajes: StateFlow<List<MensajeDto>> = _mensajes.asStateFlow()

    // Estado para controlar el indicador de carga inicial
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // 🔥 NUEVO ESTADO: Guarda la foto de perfil en Base64 del otro usuario de forma reactiva
    private val _fotoPerfilOtroUsuario = MutableStateFlow<String?>(null)
    val fotoPerfilOtroUsuario: StateFlow<String?> = _fotoPerfilOtroUsuario.asStateFlow()

    // Variables de control para el Polling
    private var miId: Int = 0
    private var otroId: Int = 0
    private var ultimoIdRecibido: Long = 0L
    private var isPollingActive = false

    // 1. Agrega este nuevo estado arriba en tu ChatViewModel
    private val _nombreOtroUsuario = MutableStateFlow<String>("Cargando...")
    val nombreOtroUsuario: StateFlow<String> = _nombreOtroUsuario.asStateFlow()

    /**
     * Inicializa el chat cargando el historial e iniciando el loop de HTTP Polling.
     */

    // 2. Modifica la función iniciarChat para que reciba también el repositorio de usuarios o use el tuyo
    fun iniciarChat(remitenteId: Int, receptorId: Int) {
        if (isPollingActive && miId == remitenteId && otroId == receptorId) return

        this.miId = remitenteId
        this.otroId = receptorId
        this.isPollingActive = true

        viewModelScope.launch {
            _isLoading.value = true

            // 🔥 Cargar de forma asíncrona los datos del perfil del receptor
            try {
                val fotoBase64 = repository.obtenerFotoPerfil(receptorId)
                _fotoPerfilOtroUsuario.value = fotoBase64

                // BUSCAR EL USERNAME REAL DESDE TU REPOSITORIO DE USUARIOS
                // Asumiendo que tienes acceso a tu UsuarioRepository aquí o mediante una llamada:
                val usuario = ni.edu.uam.michimaker.repository.UsuarioRepository().obtenerPorId(receptorId)
                if (usuario != null) {
                    _nombreOtroUsuario.value = usuario.username
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _fotoPerfilOtroUsuario.value = null
            }

            // Historial inicial
            val historial = repository.obtenerHistorial(miId, otroId)
            _mensajes.value = historial
            actualizarUltimoId(historial)

            _isLoading.value = false

            while (isPollingActive) {
                delay(2000)
                ejecutarPolling()
            }
        }
    }

    /**
     * Consulta al servidor por mensajes nuevos que tengan un ID mayor al último registrado.
     */
    private suspend fun ejecutarPolling() {
        val nuevosMensajes = repository.obtenerNuevos(miId, otroId, ultimoIdRecibido)
        if (nuevosMensajes.isNotEmpty()) {
            // Unir la lista existente con los nuevos elementos entrantes
            _mensajes.value = _mensajes.value + nuevosMensajes
            actualizarUltimoId(nuevosMensajes)
        }
    }

    /**
     * Envía un mensaje al servidor de forma asíncrona y lo añade al feed local de inmediato.
     */
    fun enviarMensaje(contenido: String) {
        if (contenido.isBlank()) return

        val nuevoMensajeDto = MensajeDto(
            remitenteId = miId,
            receptorId = otroId,
            contenido = contenido.trim()
        )

        viewModelScope.launch {
            val enviado = repository.enviarMensaje(nuevoMensajeDto)
            if (enviado != null) {
                // Agregar el mensaje enviado a la lista local para respuesta visual instantánea
                _mensajes.value = _mensajes.value + enviado
                if ((enviado.id ?: 0L) > ultimoIdRecibido) {
                    ultimoIdRecibido = enviado.id ?: ultimoIdRecibido
                }
            }
        }
    }

    /**
     * Actualiza el rastreador del último ID basándose en la lista de mensajes dada.
     */
    private fun actualizarUltimoId(lista: List<MensajeDto>) {
        val maxId = lista.maxOfOrNull { it.id ?: 0L } ?: 0L
        if (maxId > ultimoIdRecibido) {
            ultimoIdRecibido = maxId
        }
    }

    /**
     * Cancela el bucle de polling cuando el usuario sale de la pantalla.
     */
    fun detenerPolling() {
        isPollingActive = false
    }

    override fun onCleared() {
        super.onCleared()
        detenerPolling() // Asegura liberar el loop al destruir el ViewModel
    }
}