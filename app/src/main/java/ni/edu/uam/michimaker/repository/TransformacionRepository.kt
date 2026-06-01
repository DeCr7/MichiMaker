package ni.edu.uam.michimaker.repository

import kotlinx.coroutines.flow.Flow
import ni.edu.uam.michimaker.database.TransformacionDao
import ni.edu.uam.michimaker.database.TransformacionEntity

class TransformacionRepository(
    private val dao: TransformacionDao
) {

    suspend fun guardar(
        transformacion: TransformacionEntity
    ) {
        dao.insertar(transformacion)
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