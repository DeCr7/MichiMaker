package ni.edu.uam.michimaker.api

import ni.edu.uam.michimaker.dto.UsuarioDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UsuarioApi {

    @POST("usuarios")
    suspend fun registrarUsuario(
        @Body usuario: UsuarioDto
    ): Response<UsuarioDto>

    @GET("usuarios")
    suspend fun obtenerUsuarios():
            Response<List<UsuarioDto>>

    @GET("usuarios/username/{username}")
    suspend fun obtenerPorUsername(
        @Path("username")
        username: String
    ): Response<UsuarioDto>

    @POST("usuarios/login")
    suspend fun login(
        @Body usuario: UsuarioDto
    ): Response<UsuarioDto>

    @GET("usuarios/{id}")
    suspend fun obtenerPorId(
        @Path("id") id: Int
    ): Response<UsuarioDto>

    @PUT("usuarios/{id}")
    suspend fun actualizarPerfil(
        @Path("id") id: Int,
        @Body usuario: UsuarioDto
    ): Response<UsuarioDto>
}