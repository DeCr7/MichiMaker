package ni.edu.uam.michimaker.viewmodel

import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ni.edu.uam.michimaker.ia.FaceDetectorManager
import ni.edu.uam.michimaker.model.FaceDetectionState

class FaceDetectionViewModel(
    private val detector: FaceDetectorManager
) : ViewModel() {

    private val _state =
        MutableStateFlow(FaceDetectionState())

    val state: StateFlow<FaceDetectionState> =
        _state.asStateFlow()

    fun detectarRostros(
        image: InputImage
    ) {

        _state.value =
            _state.value.copy(
                cargando = true,
                mensajeError = null
            )

        detector.detectarRostros(
            image = image,

            onSuccess = { cantidad ->

                _state.value =
                    FaceDetectionState(
                        cargando = false,
                        rostrosDetectados = cantidad,
                        rostroEncontrado = cantidad > 0
                    )
            },

            onError = { exception ->

                _state.value =
                    FaceDetectionState(
                        cargando = false,
                        mensajeError = exception.message
                    )
            }
        )
    }

    fun limpiarEstado() {

        _state.value =
            FaceDetectionState()
    }
}