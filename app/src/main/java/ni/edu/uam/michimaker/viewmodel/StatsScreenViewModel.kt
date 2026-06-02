package ni.edu.uam.michimaker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ni.edu.uam.michimaker.repository.TransformacionRepository

class StatsViewModel(
    private val repository: TransformacionRepository
) : ViewModel() {

    val uiState = repository.obtenerTodas()
        .map { lista ->

            val total = lista.size

            val porFiltro = lista.groupingBy {
                it.nombreFiltro
            }.eachCount()

            StatsState(
                total = total,
                porFiltro = porFiltro
            )
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            StatsState()
        )
}