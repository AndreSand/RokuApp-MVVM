package com.android.rokuapp.data.repository

import com.android.rokuapp.data.model.App
import com.android.rokuapp.data.network.ApiService
import com.android.rokuapp.data.network.RockuApi

//suspend fun getApps(): List<App> = apiService.getApps().apps

class Repository(private val api: RockuApi = ApiService.api) {
    suspend fun getApps(): List<App> {
        return api.getApps().apps
    }
}