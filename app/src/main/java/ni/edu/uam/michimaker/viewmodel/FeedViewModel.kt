package ni.edu.uam.michimaker.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.launch
import ni.edu.uam.michimaker.api.ApiClient
import ni.edu.uam.michimaker.database.AppDatabaseProvider
import ni.edu.uam.michimaker.dto.TransformacionFeedDto
import ni.edu.uam.michimaker.repository.TransformacionRepository

class FeedViewModel(context: Context) : ViewModel() {

    private val repository = TransformacionRepository(
        AppDatabaseProvider
            .obtener(context)
            .transformacionDao()
    )

    var feed = mutableStateOf<List<TransformacionFeedDto>>(emptyList())
        private set

    var loading = mutableStateOf(false)
        private set

    fun cargarFeed() {
        viewModelScope.launch {
            loading.value = true
            feed.value = repository.obtenerFeed()
            loading.value = false
        }
    }
}