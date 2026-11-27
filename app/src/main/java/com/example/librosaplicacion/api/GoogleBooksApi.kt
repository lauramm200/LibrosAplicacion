package com.example.librosaplicacion.api

import com.example.librosaplicacion.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GoogleBooksApi {

    @GET("volumes")
    suspend fun buscar(
        @Query("q") consulta: String,
        @Query("maxResults") max: Int = 20,
        @Query("key") clave: String? = BuildConfig.BOOKS_API_KEY.ifEmpty { null }
    ): RespuestaVolumenes

    @GET("volumes/{id}")
    suspend fun porId(
        @Path("id") id: String,
        @Query("key") clave: String? = BuildConfig.BOOKS_API_KEY.ifEmpty { null }
    ): Volumen
}

object RetrofitClient {
    val api: GoogleBooksApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://www.googleapis.com/books/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GoogleBooksApi::class.java)
    }
}