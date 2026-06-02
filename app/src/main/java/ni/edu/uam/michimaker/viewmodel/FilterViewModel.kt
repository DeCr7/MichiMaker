package ni.edu.uam.michimaker.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ni.edu.uam.michimaker.model.CatFilter

class FilterViewModel : ViewModel() {

    private val _filtroSeleccionado =
        MutableStateFlow<CatFilter?>(null)

    val filtroSeleccionado =
        _filtroSeleccionado.asStateFlow()

    fun seleccionarFiltro(filtro: CatFilter) {
        _filtroSeleccionado.value = filtro
    }

    fun limpiar() {
        _filtroSeleccionado.value = null
    }
}