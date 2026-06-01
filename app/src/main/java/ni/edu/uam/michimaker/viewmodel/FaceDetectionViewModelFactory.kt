package ni.edu.uam.michimaker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ni.edu.uam.michimaker.ia.FaceDetectorManager

class FaceDetectionViewModelFactory(
    private val detector: FaceDetectorManager
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        return FaceDetectionViewModel(
            detector
        ) as T
    }
}