package ni.edu.uam.michimaker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ni.edu.uam.michimaker.repository.TransformacionRepository

class TransformacionViewModelFactory(
    private val repository: TransformacionRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        return TransformacionViewModel(
            repository
        ) as T
    }
}