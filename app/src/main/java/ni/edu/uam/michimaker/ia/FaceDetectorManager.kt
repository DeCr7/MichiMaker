package ni.edu.uam.michimaker.ia

import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions

class FaceDetectorManager {

    private val options =
        FaceDetectorOptions.Builder()
            .setPerformanceMode(
                FaceDetectorOptions
                    .PERFORMANCE_MODE_ACCURATE
            )
            .enableTracking()
            .build()

    private val detector =
        FaceDetection.getClient(
            options
        )

    fun detectarRostros(
        image: InputImage,
        onSuccess: (List<Face>) -> Unit,
        onError: (Exception) -> Unit
    ) {

        detector.process(image)
            .addOnSuccessListener {
                onSuccess(it)
            }
            .addOnFailureListener {
                onError(it)
            }
    }
}