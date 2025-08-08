package com.android.rokuapp.data.network

import retrofit2.http.GET

interface ApiService {
    @GET("apps.json")
    suspend fun getApps(): ApiResponse
}