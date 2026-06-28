package ni.edu.uam.michimaker.repository

import android.util.Log
import ni.edu.uam.michimaker.api.ApiClient
import ni.edu.uam.michimaker.dto.UsuarioDto
import ni.edu.uam.michimaker.utils.SessionManager

class UsuarioRepository {

    companion object {
        private const val TAG = "USUARIO_API"
    }

    suspend fun registrar(
        usuario: UsuarioDto
    ): Boolean {

        return try {

            val response =
                ApiClient.usuarioApi
                    .registrarUsuario(usuario)

            Log.d(
                TAG,
                "Registro usuario -> ${response.code()}"
            )

            response.isSuccessful

        } catch (e: Exception) {

            Log.e(
                TAG,
                "Error registrando usuario",
                e
            )

            false
        }
    }

    suspend fun obtenerUsuarios():
            List<UsuarioDto> {

        return try {

            val response =
                ApiClient.usuarioApi
                    .obtenerUsuarios()

            if (response.isSuccessful) {

                response.body()
                    ?: emptyList()

            } else {

                Log.e(
                    TAG,
                    "Error HTTP: ${response.code()}"
                )

                emptyList()
            }

        } catch (e: Exception) {

            Log.e(
                TAG,
                "Error obteniendo usuarios",
                e
            )

            emptyList()
        }
    }

    suspend fun obtenerPorUsername(
        username: String
    ): UsuarioDto? {

        return try {

            val response =
                ApiClient.usuarioApi
                    .obtenerPorUsername(
                        username
                    )

            if (response.isSuccessful) {

                response.body()

            } else {

                Log.e(
                    TAG,
                    "Usuario no encontrado"
                )

                null
            }

        } catch (e: Exception) {

            Log.e(
                TAG,
                "Error buscando usuario",
                e
            )

            null
        }
    }

    suspend fun obtenerPorId(id: Int): UsuarioDto? {
        return try {
            val response = ApiClient.usuarioApi.obtenerPorId(id)
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            Log.e(TAG, "Error obteniendo usuario por id", e)
            null
        }
    }

    suspend fun actualizarPerfil(id: Int, dto: UsuarioDto): Boolean {
        return try {
            // 🔥 LOG INTERCEPCIÓN DE RED: Analiza si los datos salen con éxito de Android
            Log.d(TAG, "=== TRANSMITIENDO ACTUALIZACIÓN A LA API ===")
            Log.d(TAG, "ID Usuario Target: $id")
            Log.d(TAG, "Nombre: ${dto.nombre}")
            Log.d(TAG, "Biografía: ${dto.biografia ?: "Nula/Vacía"}")
            Log.d(TAG, "Longitud Foto Perfil (Base64): ${dto.fotoPerfil?.length ?: 0}")

            val response = ApiClient.usuarioApi.actualizarPerfil(id, dto)

            Log.d(TAG, "Respuesta del Backend Código HTTP: ${response.code()}")

            if (response.isSuccessful) {
                response.body()?.let { updatedDto ->
                    Log.d(TAG, "Sincronizando sesión local en SessionManager")
                    SessionManager.guardarUsuario(updatedDto)
                }
                true
            } else {
                Log.e(TAG, "Error en el servidor al actualizar perfil: ${response.errorBody()?.string()}")
                false
            }
        } catch (e: Exception) {
            Log.e(TAG, "Fallo crítico de conexión / Red", e)
            false
        }
    }

    suspend fun login(
        username: String,
        password: String
    ): UsuarioDto? {

        return try {

            val response =
                ApiClient.usuarioApi.login(
                    UsuarioDto(
                        username = username,
                        nombre = "",
                        correo = "",
                        password = password,
                        fotoPerfil = null
                    )
                )

            if (response.isSuccessful) {

                response.body()

            } else {

                Log.e(
                    TAG,
                    "Credenciales inválidas"
                )

                null
            }

        } catch (e: Exception) {

            Log.e(
                TAG,
                "Error en login",
                e
            )

            null
        }
    }
}