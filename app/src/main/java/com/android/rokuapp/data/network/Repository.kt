package com.android.rokuapp.data.network

import com.android.rokuapp.data.model.App
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

class Repository {

    private val json = Json { ignoreUnknownKeys = true }

    private val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://rokumobileinterview.s3.us-west-2.amazonaws.com/")
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(ApiService::class.java)
    }

    suspend fun getApps(): List<App> {
        return try {
            apiService.getApps().apps
        } catch (e: Exception) {
            emptyList()
        }
    }
}