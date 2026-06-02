package ni.edu.uam.michimaker.viewmodel

import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ni.edu.uam.michimaker.ia.FaceDetectorManager
import ni.edu.uam.michimaker.model.FaceDetectionState

class FaceDetectionViewModel(
    private val detector: FaceDetectorManager
) : ViewModel() {

    private val _state =
        MutableStateFlow(
            FaceDetectionState()
        )

    val state: StateFlow<FaceDetectionState> =
        _state.asStateFlow()

    private var rostrosDetectados:
            List<Face> = emptyList()

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

            onSuccess = { faces ->

                rostrosDetectados = faces

                _state.value =
                    FaceDetectionState(
                        cargando = false,
                        rostrosDetectados =
                            faces.size,
                        rostroEncontrado =
                            faces.isNotEmpty(),
                        mensajeError = null
                    )
            },

            onError = { exception ->

                rostrosDetectados =
                    emptyList()

                _state.value =
                    FaceDetectionState(
                        cargando = false,
                        rostrosDetectados = 0,
                        rostroEncontrado = false,
                        mensajeError =
                            exception.message
                                ?: "Error desconocido"
                    )
            }
        )
    }

    fun obtenerRostros():
            List<Face> {

        return rostrosDetectados
    }

    fun limpiarEstado() {

        rostrosDetectados =
            emptyList()

        _state.value =
            FaceDetectionState()
    }
}