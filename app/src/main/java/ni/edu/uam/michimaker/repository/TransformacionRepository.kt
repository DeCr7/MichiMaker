package ni.edu.uam.michimaker.repository

import android.util.Log
import ni.edu.uam.michimaker.api.ApiClient
import ni.edu.uam.michimaker.dto.TransformacionDto
import ni.edu.uam.michimaker.dto.TransformacionFeedDto

class TransformacionRepository {


    // =========================
    // GUARDAR EN RENDER
    // =========================

    suspend fun guardar(
        transformacion: TransformacionDto
    ): Boolean {

        return try {

            val response =
                ApiClient.api
                    .guardarTransformacion(
                        transformacion
                    )

            if (response.isSuccessful) {

                Log.d(
                    "API",
                    "Transformación guardada correctamente"
                )

                true

            } else {

                Log.e(
                    "API",
                    "Error HTTP ${response.code()}"
                )

                false
            }


        } catch (e: Exception) {

            Log.e(
                "API_ERROR",
                "Error guardando transformación",
                e
            )

            false
        }
    }



    // =========================
    // FEED GLOBAL
    // =========================

    suspend fun obtenerFeed():
            List<TransformacionFeedDto> {

        return try {

            val response =
                ApiClient.api.obtenerFeed()


            if(response.isSuccessful){

                response.body()
                    ?: emptyList()

            } else {

                emptyList()
            }


        } catch (e: Exception) {

            Log.e(
                "API_ERROR",
                "Error obteniendo feed",
                e
            )

            emptyList()
        }
    }



    // =========================
    // HISTORIAL POR USUARIO
    // =========================

// =========================
// HISTORIAL POR USUARIO
// =========================

    suspend fun obtenerPorUsuario(
        usuarioId: Int
    ): List<TransformacionFeedDto> {

        return try {

            val response =
                ApiClient.api.obtenerPorUsuario(
                    usuarioId
                )

            if (response.isSuccessful) {

                response.body()
                    ?: emptyList()

            } else {

                Log.e(
                    "API",
                    "Error HTTP ${response.code()}"
                )

                emptyList()
            }


        } catch(e: Exception){

            Log.e(
                "API_ERROR",
                "Error obteniendo historial",
                e
            )

            emptyList()
        }
    }

    suspend fun limpiarPorUsuario(
        usuarioId: Int
    ) {

        try {

            val response =
                ApiClient.api.eliminarPorUsuario(
                    usuarioId
                )

            if (!response.isSuccessful) {

                Log.e(
                    "API",
                    "Error eliminando historial"
                )
            }

        } catch(e: Exception) {

            Log.e(
                "API_ERROR",
                "Error eliminando historial",
                e
            )
        }
    }
}