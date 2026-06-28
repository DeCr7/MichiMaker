package ni.edu.uam.michimaker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ni.edu.uam.michimaker.dto.TransformacionDto
import ni.edu.uam.michimaker.dto.TransformacionFeedDto
import ni.edu.uam.michimaker.repository.TransformacionRepository
import ni.edu.uam.michimaker.utils.TransformacionUiState

class TransformacionViewModel(
    private val repository: TransformacionRepository,
    private val usuarioId: Int
) : ViewModel() {

    private val _uiState = MutableStateFlow(TransformacionUiState(cargando = true))
    val uiState: StateFlow<TransformacionUiState> = _uiState

    // Estado para almacenar las publicaciones globales que vienen de Render
    private val _feedState = MutableStateFlow<List<TransformacionFeedDto>>(emptyList())
    val feedState: StateFlow<List<TransformacionFeedDto>> = _feedState

    private val _isRefreshingFeed = MutableStateFlow(false)
    val isRefreshingFeed: StateFlow<Boolean> = _isRefreshingFeed

    init {
        cargarHistorial()
        cargarFeedGlobal()
    }

    private fun cargarHistorial() {
        viewModelScope.launch {
            try {
                val transformaciones = repository.obtenerPorUsuario(usuarioId)
                _uiState.value = TransformacionUiState(
                    cargando = false,
                    transformaciones = transformaciones,
                    total = transformaciones.size
                )
            } catch (e: Exception) {
                _uiState.value = TransformacionUiState(cargando = false, error = e.message)
            }
        }
    }

    // CORRECCIÓN: Ahora usa exactamente repository.obtenerFeed()
    fun cargarFeedGlobal() {
        viewModelScope.launch {
            _isRefreshingFeed.value = true
            try {
                val feedCompleto = repository.obtenerFeed()
                _feedState.value = feedCompleto
            } catch (e: Exception) {
                android.util.Log.e("FEED_ERROR", "Error cargando feed global: ${e.message}")
            } finally {
                _isRefreshingFeed.value = false
            }
        }
    }

    fun actualizarLeyenda(id: Int, nuevaLeyenda: String) {
        viewModelScope.launch {
            try {
                repository.actualizarLeyenda(id, nuevaLeyenda)
                _uiState.update { estado ->
                    estado.copy(
                        transformaciones = estado.transformaciones.map { item ->
                            if(item.id == id) item.copy(leyenda = nuevaLeyenda) else item
                        }
                    )
                }
                _feedState.update { lista ->
                    lista.map { item ->
                        if(item.id == id) item.copy(leyenda = nuevaLeyenda) else item
                    }
                }
            } catch(e: Exception) {
                _uiState.update { estado -> estado.copy(error = e.message) }
            }
        }
    }

    fun eliminarTransformacion(id: Int) {
        viewModelScope.launch {
            try {
                repository.eliminarTransformacion(id)
                _uiState.update { estado ->
                    estado.copy(
                        transformaciones = estado.transformaciones.filter { it.id != id },
                        total = estado.total - 1
                    )
                }
                _feedState.update { lista -> lista.filter { it.id != id } }
            } catch(e: Exception) {
                _uiState.update { estado -> estado.copy(error = e.message) }
            }
        }
    }

    fun guardar(transformacion: TransformacionDto) {
        viewModelScope.launch {
            repository.guardar(transformacion)
            cargarHistorial()
            cargarFeedGlobal()
        }
    }

    fun recargar() {
        cargarHistorial()
        cargarFeedGlobal()
    }
}