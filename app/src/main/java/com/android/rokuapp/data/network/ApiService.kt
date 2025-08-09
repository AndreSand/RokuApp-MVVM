package com.android.rokuapp.data.network

import com.android.rokuapp.data.model.App
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.http.GET

interface ApiService {
    @GET("apps.json")
    suspend fun getApps(): ApiResponse

    companion object {
        const val BASE_URL = "https://rokumobileinterview.s3.us-west-2.amazonaws.com/"
    }
}

@Serializable
data class ApiResponse(
    @SerialName("apps")
    val apps: List<App> = emptyList()
)