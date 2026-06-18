package ni.edu.uam.michimaker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ni.edu.uam.michimaker.dto.TransformacionDto
import ni.edu.uam.michimaker.ia.CatFilterManager
import ni.edu.uam.michimaker.ia.FaceDetectorManager
import ni.edu.uam.michimaker.ia.OverlayManager
import ni.edu.uam.michimaker.repository.TransformacionRepository
import ni.edu.uam.michimaker.storage.ImageStorageManager
import ni.edu.uam.michimaker.utils.DateUtils
import ni.edu.uam.michimaker.utils.ResultState


class ResultViewModel(
    application: Application
) : AndroidViewModel(application) {


    private val repository =
        TransformacionRepository()


    private val filterManager: CatFilterManager


    private val _state =
        MutableStateFlow(ResultState())


    val state =
        _state.asStateFlow()



    init {

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


                val transformacion =
                    TransformacionDto(

                        id = null,

                        nombreFiltro = filtro,

                        fecha =
                            DateUtils.fechaActual(),

                        rutaImagen = rutaImagen,

                        usuarioId = usuarioId,

                        leyenda = leyenda
                    )


                repository.guardar(
                    transformacion
                )


            } catch (e: Exception) {

                e.printStackTrace()
            }
        }
    }
}