package ni.edu.uam.michimaker.dto

import com.google.gson.annotations.SerializedName

data class ChatPreviewDto(
    @SerializedName("usuarioId") val usuarioId: Int,
    @SerializedName("username") val username: String?,
    @SerializedName("fotoPerfil") val fotoPerfil: String?,
    @SerializedName("ultimoMensaje") val ultimoMensaje: String,
    @SerializedName("fechaEnvio") val fechaEnvio: String,
    @SerializedName("leido") val leido: Boolean


)