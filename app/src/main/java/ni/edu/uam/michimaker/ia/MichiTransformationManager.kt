package ni.edu.uam.michimaker.ia

import ni.edu.uam.michimaker.dto.TransformacionDto
import ni.edu.uam.michimaker.repository.TransformacionRepository

class MichiTransformationManager(

    private val detector: FaceDetectorManager,
    private val filterManager: CatFilterManager,
    private val repository: TransformacionRepository

) {

    suspend fun guardarTransformacion(
        transformacion: TransformacionDto
    ) {

        repository.guardar(
            transformacion
        )
    }
}