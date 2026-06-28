package ni.edu.uam.michimaker.api

import ni.edu.uam.michimaker.dto.ChatPreviewDto
import ni.edu.uam.michimaker.dto.MensajeDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface MensajeApi {

    @POST("api/mensajes/enviar")
    suspend fun enviarMensaje(@Body dto: MensajeDto): Response<MensajeDto>

    @GET("api/mensajes/historial")
    suspend fun obtenerHistorial(
        @Query("user1") user1: Int,
        @Query("user2") user2: Int
    ): Response<List<MensajeDto>>

    @GET("api/mensajes/nuevos")
    suspend fun obtenerNuevos(
        @Query("remitente") remitente: Int,
        @Query("receptor") receptor: Int,
        @Query("ultimoId") ultimoId: Long
    ): Response<List<MensajeDto>>

    // 🔥 EL NUEVO ENDPOINT VINCULADO AL BACKEND:
    @GET("api/mensajes/bandeja/{userId}")
    suspend fun obtenerBandejaEntrada(
        @Path("userId") userId: Int
    ): Response<List<ChatPreviewDto>>
}