package ni.edu.uam.michimaker.repository

import android.util.Log
import kotlinx.coroutines.flow.Flow
import ni.edu.uam.michimaker.api.ApiClient
import ni.edu.uam.michimaker.database.TransformacionDao
import ni.edu.uam.michimaker.database.TransformacionEntity
import ni.edu.uam.michimaker.dto.TransformacionDto
import ni.edu.uam.michimaker.dto.TransformacionFeedDto

class TransformacionRepository(
    private val dao: TransformacionDao
) {

    suspend fun guardar(
        transformacion: TransformacionEntity
    ) {

        try {

            val dto =
                TransformacionDto(
                    id = null,
                    nombreFiltro = transformacion.nombreFiltro,
                    fecha = transformacion.fecha,
                    rutaImagen = transformacion.rutaImagen,
                    usuarioId = transformacion.usuarioId,
                    leyenda = transformacion.leyenda
                )

            Log.d("API", "DTO: $dto")

            val response =
                ApiClient.api
                    .guardarTransformacion(dto)

            if (response.isSuccessful) {

                Log.d(
                    "API",
                    "✔ Guardado en backend"
                )

                dao.insertar(transformacion)

            } else {

                Log.e(
                    "API",
                    "Error HTTP: ${
                        response.errorBody()?.string()
                    }"
                )
            }

        } catch (e: Exception) {

            Log.e(
                "API_ERROR",
                "Error de red",
                e
            )
        }
    }

    suspend fun limpiarTodo() {
        dao.eliminarTodo()
    }

    suspend fun limpiarPorUsuario(
        usuarioId: Int
    ) {

        dao.eliminarPorUsuario(
            usuarioId
        )
    }

    suspend fun obtenerFeed(): List<TransformacionFeedDto> {
        return try {
            val response = ApiClient.api.obtenerFeed()
            response.body() ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun eliminar(transformacion: TransformacionEntity) {
        dao.eliminar(transformacion)
    }

    fun obtenerTodas(): Flow<List<TransformacionEntity>> {
        return dao.obtenerTodas()
    }

    fun contar(): Flow<Int> {
        return dao.contarTransformaciones()
    }

    fun obtenerPorUsuario(
        usuarioId: Int
    ): Flow<List<TransformacionEntity>> {

        return dao.obtenerPorUsuario(
            usuarioId
        )
    }

    fun contarPorUsuario(
        usuarioId: Int
    ): Flow<Int> {

        return dao.contarPorUsuario(
            usuarioId
        )
    }
}