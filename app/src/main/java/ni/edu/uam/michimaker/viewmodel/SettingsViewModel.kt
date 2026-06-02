package ni.edu.uam.michimaker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ni.edu.uam.michimaker.repository.TransformacionRepository

class SettingsViewModel(
    private val repository: TransformacionRepository
) : ViewModel() {

    fun limpiarHistorial(onDone: () -> Unit) {
        viewModelScope.launch {
            repository.limpiarTodo()
            onDone()
        }
    }
}