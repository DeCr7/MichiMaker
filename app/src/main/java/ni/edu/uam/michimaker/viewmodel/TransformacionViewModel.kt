package ni.edu.uam.michimaker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ni.edu.uam.michimaker.database.TransformacionEntity
import ni.edu.uam.michimaker.repository.TransformacionRepository
import ni.edu.uam.michimaker.utils.TransformacionUiState

class TransformacionViewModel(
    private val repository: TransformacionRepository
) : ViewModel() {

    val uiState: StateFlow<TransformacionUiState> =
        combine(
            repository.obtenerTodas(),
            repository.contar()
        ) { transformaciones, total ->

            TransformacionUiState(
                cargando = false,
                transformaciones = transformaciones,
                total = total,
                error = null
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = TransformacionUiState(
                cargando = true
            )
        )

    fun guardar(
        transformacion: TransformacionEntity
    ) {
        viewModelScope.launch {
            try {

                repository.guardar(
                    transformacion
                )

            } catch (e: Exception) {

                e.printStackTrace()
            }
        }
    }

    fun eliminar(
        transformacion: TransformacionEntity
    ) {
        viewModelScope.launch {
            try {

                repository.eliminar(
                    transformacion
                )

            } catch (e: Exception) {

                e.printStackTrace()
            }
        }
    }
}