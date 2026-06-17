package ni.edu.uam.michimaker.repository

import android.util.Log
import ni.edu.uam.michimaker.api.ApiClient
import ni.edu.uam.michimaker.dto.UsuarioDto

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