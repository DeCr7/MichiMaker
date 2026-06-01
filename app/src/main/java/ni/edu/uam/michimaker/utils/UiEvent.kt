package ni.edu.uam.michimaker.utils

sealed class UiEvent {

    data class ShowMessage(
        val message: String
    ) : UiEvent()

    object NavigateBack : UiEvent()
}