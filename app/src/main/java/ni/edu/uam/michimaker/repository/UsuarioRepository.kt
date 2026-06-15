package ni.edu.uam.michimaker.repository

import android.util.Log
import ni.edu.uam.michimaker.api.ApiClient
import ni.edu.uam.michimaker.dto.UsuarioDto

class UsuarioRepository {

    suspend fun registrar(
        usuario: UsuarioDto
    ): Boolean {

        return try {

            val response =
                ApiClient.usuarioApi
                    .registrarUsuario(usuario)

            response.isSuccessful

        } catch (e: Exception) {

            Log.e(
                "USUARIO_API",
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

            response.body()
                ?: emptyList()

        } catch (e: Exception) {

            Log.e(
                "USUARIO_API",
                "Error obteniendo usuarios",
                e
            )

            emptyList()
        }
    }
}