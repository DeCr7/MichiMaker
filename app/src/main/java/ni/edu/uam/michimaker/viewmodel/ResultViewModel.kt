package ni.edu.uam.michimaker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ni.edu.uam.michimaker.database.AppDatabaseProvider
import ni.edu.uam.michimaker.database.TransformacionEntity
import ni.edu.uam.michimaker.ia.CatFilterManager
import ni.edu.uam.michimaker.ia.FaceDetectorManager
import ni.edu.uam.michimaker.ia.OverlayManager
import ni.edu.uam.michimaker.repository.TransformacionRepository
import ni.edu.uam.michimaker.storage.ImageStorageManager
import ni.edu.uam.michimaker.utils.DateUtils
import ni.edu.uam.michimaker.utils.ResultState
import ni.edu.uam.michimaker.utils.SessionManager

class ResultViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository: TransformacionRepository

    private val filterManager: CatFilterManager

    private val _state =
        MutableStateFlow(ResultState())

    val state =
        _state.asStateFlow()

    init {

        val database =
            AppDatabaseProvider.obtener(application)

        repository =
            TransformacionRepository(
                database.transformacionDao()
            )

        filterManager =
            CatFilterManager(
                context = application,
                detector = FaceDetectorManager(),
                overlayManager = OverlayManager(),
                storageManager = ImageStorageManager(application)
            )
    }

    fun procesarImagen(
        ruta: String,
        filtro: String
    ) {

        viewModelScope.launch {

            try {

                _state.value =
                    _state.value.copy(
                        loading = true,
                        error = null
                    )

                val resultado =
                    filterManager.aplicarFiltroSuspend(
                        rutaImagen = ruta,
                        filtro = filtro
                    )

                _state.value =
                    _state.value.copy(
                        loading = false,
                        resultadoImagen = resultado
                    )

            } catch (e: Exception) {

                _state.value =
                    _state.value.copy(
                        loading = false,
                        error = e.message
                    )
            }
        }
    }

    fun guardarTransformacion(
        filtro: String,
        rutaImagen: String,
        usuarioId: Int,
        leyenda: String
    ) {

        viewModelScope.launch {

            try {

                repository.guardar(
                    TransformacionEntity(
                        nombreFiltro = filtro,
                        fecha = DateUtils.fechaActual(),
                        rutaImagen = rutaImagen,
                        usuarioId = usuarioId,
                        leyenda = leyenda
                    )
                )

            } catch (e: Exception) {

                e.printStackTrace()
            }
        }
    }
}