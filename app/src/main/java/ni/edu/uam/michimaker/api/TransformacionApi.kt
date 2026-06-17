package ni.edu.uam.michimaker.api

import ni.edu.uam.michimaker.dto.TransformacionDto
import ni.edu.uam.michimaker.dto.TransformacionFeedDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface TransformacionApi {

    @POST("transformaciones")
    suspend fun guardarTransformacion(
        @Body transformacion: TransformacionDto
    ): Response<TransformacionDto>

    @GET("transformaciones")
    suspend fun obtenerTransformaciones():
            Response<List<TransformacionDto>>

    @GET("transformaciones/feed")
    suspend fun obtenerFeed(): Response<List<TransformacionFeedDto>>

    @GET("transformaciones/usuario/{id}")
    suspend fun obtenerPorUsuario(
        @Path("id") usuarioId: Int
    ): Response<List<TransformacionDto>>
}