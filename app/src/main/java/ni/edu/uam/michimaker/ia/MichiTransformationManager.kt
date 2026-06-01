package ni.edu.uam.michimaker.ia

import ni.edu.uam.michimaker.database.TransformacionEntity
import ni.edu.uam.michimaker.repository.TransformacionRepository

class MichiTransformationManager(

    private val detector: FaceDetectorManager,
    private val filterManager: CatFilterManager,
    private val repository: TransformacionRepository
) {

    suspend fun guardarTransformacion(
        transformacion: TransformacionEntity
    ) {

        repository.guardar(transformacion)
    }
}