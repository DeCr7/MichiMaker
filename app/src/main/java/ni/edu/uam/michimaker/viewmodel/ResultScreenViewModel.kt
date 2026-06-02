package ni.edu.uam.michimaker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ni.edu.uam.michimaker.database.TransformacionEntity
import ni.edu.uam.michimaker.ia.CatFilterManager
import ni.edu.uam.michimaker.repository.TransformacionRepository
import ni.edu.uam.michimaker.utils.DateUtils
import ni.edu.uam.michimaker.utils.ResultState

class ResultViewModel(
    private val filterManager: CatFilterManager,
    private val repository: TransformacionRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ResultState())
    val state = _state.asStateFlow()

    fun procesarImagen(ruta: String, filtro: String) {

        viewModelScope.launch {

            try {
                _state.value = _state.value.copy(
                    loading = true,
                    error = null
                )

                val resultado = filterManager.aplicarFiltroSuspend(
                    rutaImagen = ruta,
                    filtro = filtro
                )

                val entity = TransformacionEntity(
                    nombreFiltro = filtro,
                    fecha = DateUtils.fechaActual(),
                    rutaImagen = resultado
                )

                repository.guardar(entity)

                _state.value = _state.value.copy(
                    loading = false,
                    resultadoImagen = resultado
                )

            } catch (e: Exception) {

                _state.value = _state.value.copy(
                    loading = false,
                    error = e.message
                )
            }
        }
    }
}