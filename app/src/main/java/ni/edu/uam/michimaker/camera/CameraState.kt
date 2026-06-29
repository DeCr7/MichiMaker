package ni.edu.uam.michimaker.camera

data class CameraState(
    val imagePath: String? = null,
    val hasPermission: Boolean = false,
    val isCapturing: Boolean = false,
    val error: String? = null
)