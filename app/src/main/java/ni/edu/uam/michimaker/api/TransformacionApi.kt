package ni.edu.uam.michimaker.api

import ni.edu.uam.michimaker.dto.TransformacionDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface TransformacionApi {

    @POST("transformaciones")
    suspend fun guardarTransformacion(
        @Body transformacion: TransformacionDto
    ): Response<TransformacionDto>

    @GET("transformaciones")
    suspend fun obtenerTransformaciones():
            Response<List<TransformacionDto>>
}