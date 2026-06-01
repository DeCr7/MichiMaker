package ni.edu.uam.michimaker.ia

import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection

class FaceDetectorManager {

    private val detector =
        FaceDetection.getClient()

    fun detectarRostros(
        image: InputImage,
        onSuccess: (List<Face>) -> Unit,
        onError: (Exception) -> Unit
    ) {

        detector.process(image)
            .addOnSuccessListener { faces ->
                onSuccess(faces)
            }
            .addOnFailureListener {
                onError(it)
            }
    }
}