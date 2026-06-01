package ni.edu.uam.michimaker.utils

sealed class FaceDetectionEvent {

    object RostroDetectado :
        FaceDetectionEvent()

    object RostroNoDetectado :
        FaceDetectionEvent()

    data class Error(
        val mensaje: String
    ) : FaceDetectionEvent()
}