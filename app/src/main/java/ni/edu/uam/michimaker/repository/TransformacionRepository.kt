package ni.edu.uam.michimaker.repository

import android.util.Log
import kotlinx.coroutines.flow.Flow
import ni.edu.uam.michimaker.api.ApiClient
import ni.edu.uam.michimaker.database.TransformacionDao
import ni.edu.uam.michimaker.database.TransformacionEntity
import ni.edu.uam.michimaker.dto.TransformacionDto

class TransformacionRepository(
    private val dao: TransformacionDao
) {

    suspend fun guardar(transformacion: TransformacionEntity) {

        dao.insertar(transformacion)

        try {

            val dto = TransformacionDto(
                id = transformacion.id,
                nombreFiltro = transformacion.nombreFiltro,
                fecha = transformacion.fecha,
                rutaImagen = transformacion.rutaImagen
            )

            Log.d("API", "=== INICIO REQUEST ===")
            Log.d("API", "DTO: $dto")

            val response = ApiClient.api.guardarTransformacion(dto)

            Log.d("API", "CODE: ${response.code()}")
            Log.d("API", "SUCCESS: ${response.isSuccessful}")

            if (response.isSuccessful) {
                Log.d("API", "✔ ENVIADO OK")
            } else {
                Log.e("API", "✘ ERROR HTTP: ${response.errorBody()?.string()}")
            }

        } catch (e: Exception) {
            Log.e("API_ERROR", "✘ ERROR DE RED / CONEXIÓN", e)
        }
    }

    suspend fun limpiarTodo() {
        dao.eliminarTodo()
    }

    suspend fun eliminar(
        transformacion: TransformacionEntity
    ) {
        dao.eliminar(transformacion)
    }

    fun obtenerTodas():
            Flow<List<TransformacionEntity>> {

        return dao.obtenerTodas()
    }

    fun contar():
            Flow<Int> {

        return dao.contarTransformaciones()
    }
}