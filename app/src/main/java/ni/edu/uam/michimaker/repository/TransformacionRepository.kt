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

            Log.d(
                "API_SEND",
                transformacion.toString()
            )

            val response =
                ApiClient.api
                    .guardarTransformacion(
                        transformacion
                    )


            val errorBody =
                response.errorBody()?.string()


            Log.d(
                "API_CODE",
                response.code().toString()
            )


            Log.d(
                "API_BODY",
                response.body().toString()
            )


            Log.d(
                "API_ERROR_BODY",
                errorBody ?: "Sin error"
            )


            if (response.isSuccessful) {

                Log.d(
                    "API",
                    "Transformación guardada correctamente"
                )

                true

            } else {

                false
            }


        } catch (e: Exception) {

            Log.e(
                "API_EXCEPTION",
                "Excepción",
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

    suspend fun actualizarLeyenda(
        id: Int,
        leyenda: String
    ): Boolean {

        return try {

            val dto = TransformacionDto(

                id = id,

                nombreFiltro = "",

                fecha = "",

                rutaImagen = "",

                usuarioId = 0,

                leyenda = leyenda,

                imagenBase64 = null
            )

            val response =
                ApiClient.api.actualizarLeyenda(
                    id,
                    dto
                )

            response.isSuccessful

        } catch (e: Exception) {

            false
        }
    }

    suspend fun eliminarTransformacion(
        id: Int
    ): Boolean {

        return try {

            val response =
                ApiClient.api.eliminarTransformacion(
                    id
                )

            response.isSuccessful

        } catch (e: Exception) {

            false
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