package ni.edu.uam.michimaker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ni.edu.uam.michimaker.dto.TransformacionDto
import ni.edu.uam.michimaker.repository.TransformacionRepository
import ni.edu.uam.michimaker.utils.TransformacionUiState


class TransformacionViewModel(
    private val repository: TransformacionRepository,
    private val usuarioId: Int
) : ViewModel() {


    private val _uiState =
        MutableStateFlow(
            TransformacionUiState(
                cargando = true
            )
        )

    val uiState: StateFlow<TransformacionUiState> =
        _uiState


    init {
        cargarHistorial()
    }


    private fun cargarHistorial() {

        viewModelScope.launch {

            try {

                val transformaciones =
                    repository.obtenerPorUsuario(
                        usuarioId
                    )


                _uiState.value =
                    TransformacionUiState(

                        cargando = false,

                        transformaciones =
                            transformaciones,

                        total =
                            transformaciones.size
                    )


            } catch (e: Exception) {

                _uiState.value =
                    TransformacionUiState(

                        cargando = false,

                        error = e.message
                    )
            }
        }
    }


    fun guardar(
        transformacion: TransformacionDto
    ) {

        viewModelScope.launch {

            repository.guardar(
                transformacion
            )

            cargarHistorial()
        }
    }


    fun recargar() {

        cargarHistorial()

    }
}