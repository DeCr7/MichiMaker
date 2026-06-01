package ni.edu.uam.michimaker.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ni.edu.uam.michimaker.camera.CameraState

class CameraViewModel : ViewModel() {

    private val _cameraState =
        MutableStateFlow(CameraState())

    val cameraState: StateFlow<CameraState> =
        _cameraState.asStateFlow()

    fun iniciarCaptura() {

        _cameraState.value =
            _cameraState.value.copy(
                isCapturing = true,
                error = null
            )
    }

    fun capturaExitosa(
        rutaImagen: String
    ) {

        _cameraState.value =
            _cameraState.value.copy(
                imagePath = rutaImagen,
                isCapturing = false
            )
    }

    fun capturaFallida(
        mensaje: String
    ) {

        _cameraState.value =
            _cameraState.value.copy(
                isCapturing = false,
                error = mensaje
            )
    }

    fun actualizarPermiso(
        concedido: Boolean
    ) {

        _cameraState.value =
            _cameraState.value.copy(
                hasPermission = concedido
            )
    }

    fun guardarImagen(
        rutaImagen: String
    ) {

        _cameraState.value =
            _cameraState.value.copy(
                imagePath = rutaImagen
            )
    }

    fun limpiarImagen() {

        _cameraState.value =
            _cameraState.value.copy(
                imagePath = null
            )
    }

    fun reset() {

        _cameraState.value =
            CameraState()
    }
}