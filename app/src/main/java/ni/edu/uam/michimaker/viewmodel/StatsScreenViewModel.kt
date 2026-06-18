package ni.edu.uam.michimaker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ni.edu.uam.michimaker.repository.TransformacionRepository


class StatsViewModel(
    private val repository: TransformacionRepository
) : ViewModel() {


    private val _uiState =
        MutableStateFlow(
            StatsState()
        )

    val uiState: StateFlow<StatsState> =
        _uiState


    init {

        cargarEstadisticas()

    }


    private fun cargarEstadisticas() {

        viewModelScope.launch {

            try {

                val lista =
                    repository.obtenerFeed()


                val total =
                    lista.size


                val porFiltro =
                    lista.groupingBy {

                        it.nombreFiltro

                    }.eachCount()


                _uiState.value =
                    StatsState(

                        total = total,

                        porFiltro = porFiltro
                    )


            } catch (e: Exception) {

                e.printStackTrace()

                _uiState.value =
                    StatsState()
            }
        }
    }


    fun recargar() {

        cargarEstadisticas()

    }
}