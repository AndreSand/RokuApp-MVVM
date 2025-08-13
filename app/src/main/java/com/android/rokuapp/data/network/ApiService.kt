package com.android.rokuapp.data.network

import com.android.rokuapp.data.model.ApiResponse
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.GET

interface RockuApi {
    @GET("apps.json")
    suspend fun getApps(): ApiResponse
}

object ApiService {
    const val BASE_URL = "https://rokumobileinterview.s3.us-west-2.amazonaws.com/"

    private val json = Json { ignoreUnknownKeys = true }

   val api: RockuApi by lazy {
        Retrofit.Builder()
            .baseUrl(ApiService.BASE_URL)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(RockuApi::class.java)
    }

}
