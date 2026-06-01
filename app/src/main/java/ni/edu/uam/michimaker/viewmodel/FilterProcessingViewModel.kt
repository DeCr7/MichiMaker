package ni.edu.uam.michimaker.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ni.edu.uam.michimaker.utils.FilterState

class FilterProcessingViewModel : ViewModel() {

    private val _state =
        MutableStateFlow(FilterState())

    val state: StateFlow<FilterState> =
        _state.asStateFlow()

    fun iniciarProcesamiento(
        rutaImagen: String,
        filtro: String
    ) {

        _state.value =
            _state.value.copy(
                procesando = true,
                imagenOriginal = rutaImagen,
                filtroAplicado = filtro,
                error = null
            )
    }

    fun procesamientoExitoso(
        rutaResultado: String
    ) {

        _state.value =
            _state.value.copy(
                procesando = false,
                imagenTransformada = rutaResultado
            )
    }

    fun procesamientoFallido(
        mensaje: String
    ) {

        _state.value =
            _state.value.copy(
                procesando = false,
                error = mensaje
            )
    }

    fun limpiarResultado() {

        _state.value =
            _state.value.copy(
                imagenTransformada = null
            )
    }

    fun reset() {

        _state.value =
            FilterState()
    }
}