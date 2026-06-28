package ni.edu.uam.michimaker.repository

import android.util.Log
import ni.edu.uam.michimaker.api.ApiClient
import ni.edu.uam.michimaker.dto.MensajeDto

class ChatRepository {

    private val api = ApiClient.mensajeApi
    // 🔥 Usamos la API de usuarios que ya tienes declarada en tu ApiClient
    private val usuarioApi = ApiClient.usuarioApi

    suspend fun enviarMensaje(dto: MensajeDto): MensajeDto? {
        return try {
            val response = api.enviarMensaje(dto)
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            Log.e("ChatRepository", "Error al enviar mensaje", e)
            null
        }
    }

    suspend fun obtenerHistorial(user1: Int, user2: Int): List<MensajeDto> {
        return try {
            val response = api.obtenerHistorial(user1, user2)
            if (response.isSuccessful) response.body() ?: emptyList() else emptyList()
        } catch (e: Exception) {
            Log.e("ChatRepository", "Error al obtener historial", e)
            emptyList()
        }
    }

    suspend fun obtenerNuevos(remitente: Int, receptor: Int, ultimoId: Long): List<MensajeDto> {
        return try {
            val response = api.obtenerNuevos(remitente, receptor, ultimoId)
            if (response.isSuccessful) response.body() ?: emptyList() else emptyList()
        } catch (e: Exception) {
            Log.e("ChatRepository", "Error al hacer polling de mensajes", e)
            emptyList()
        }
    }

    // 🔥 NUEVA FUNCIÓN: Resuelve el conflicto del ViewModel buscando en tu endpoint de usuario
    suspend fun obtenerFotoPerfil(usuarioId: Int): String? {
        return try {
            // ⚠️ AJUSTA AQUÍ: Cambia "obtenerUsuarioPorId" por el nombre real de tu función en UsuarioApi
            val response = usuarioApi.obtenerPorId(usuarioId)

            if (response.isSuccessful) {
                // ⚠️ AJUSTA AQUÍ: Cambia "fotoPerfil" por la propiedad exacta de tu DTO de usuario
                response.body()?.fotoPerfil
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("ChatRepository", "Error al obtener foto de perfil del usuario $usuarioId", e)
            null
        }
    }
}