package ni.edu.uam.michimaker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ni.edu.uam.michimaker.model.Usuario
import ni.edu.uam.michimaker.repository.UsuarioRepository

data class SearchUiState(
    val query: String = "",
    val usuariosEncontrados: List<Usuario> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@OptIn(FlowPreview::class)
class SearchUserViewModel : ViewModel() {

    private val usuarioRepo = UsuarioRepository()

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private val _queryFlow = MutableStateFlow("")

    init {
        viewModelScope.launch {
            _queryFlow
                .debounce(500)
                .filter { it.trim().length >= 3 }
                .distinctUntilChanged()
                .collect { query ->
                    ejecutarBusqueda(query)
                }
        }
    }

    fun onQueryChanged(newQuery: String) {
        _uiState.update { it.copy(query = newQuery, error = null) }
        _queryFlow.value = newQuery

        if (newQuery.trim().isEmpty()) {
            _uiState.update { it.copy(usuariosEncontrados = emptyList(), isLoading = false) }
        }
    }

    private fun ejecutarBusqueda(query: String) {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            try {
                // 1. Usamos obtenerUsuarios() que ya existe en tu UsuarioRepository
                val todosLosUsuariosDto = usuarioRepo.obtenerUsuarios()

                // 2. Filtramos localmente los que coincidan con el username o nombre
                val filtrados = todosLosUsuariosDto.filter { dto ->
                    dto.username.contains(query, ignoreCase = true) ||
                            dto.nombre.contains(query, ignoreCase = true)
                }

                // 3. Mapeamos de forma segura a tu modelo de UI "Usuario"
                val listaUsuarios = filtrados.map { dto ->
                    Usuario(
                        id = dto.id ?: 0,
                        username = dto.username,
                        nombre = dto.nombre,
                        biografia = dto.biografia ?: "",
                        correo = dto.correo,
                        fotoPerfil = dto.fotoPerfil
                    )
                }

                _uiState.update {
                    it.copy(usuariosEncontrados = listaUsuarios, isLoading = false)
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(error = e.message ?: "Error al buscar usuarios", isLoading = false)
                }
            }
        }
    }
}