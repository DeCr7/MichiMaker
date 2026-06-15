package ni.edu.uam.michimaker.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private const val BASE_URL =
        "http://10.121.36.146:8080/"

    private val retrofit: Retrofit by lazy {

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()
    }

    val api: TransformacionApi by lazy {

        retrofit.create(
            TransformacionApi::class.java
        )
    }

    val usuarioApi: UsuarioApi by lazy {

        retrofit.create(
            UsuarioApi::class.java
        )
    }
}