package ni.edu.uam.michimaker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ni.edu.uam.michimaker.api.ApiClient
import ni.edu.uam.michimaker.dto.ChatPreviewDto // 🔥 Tu nuevo DTO importado

class ChatsListViewModel(private val userId: Int) : ViewModel() {
    private val _chats = MutableStateFlow<List<ChatPreviewDto>>(emptyList())
    val chats: StateFlow<List<ChatPreviewDto>> = _chats

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    init {
        cargarBandeja()
    }

    fun cargarBandeja() {
        viewModelScope.launch {
            _isRefreshing.value = true
            try {
                val response = ApiClient.mensajeApi.obtenerBandejaEntrada(userId)
                if (response.isSuccessful) {
                    _chats.value = response.body() ?: emptyList()
                } else {
                    _chats.value = emptyList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _chats.value = emptyList()
            } finally {
                _isRefreshing.value = false
            }
        }
    }
}