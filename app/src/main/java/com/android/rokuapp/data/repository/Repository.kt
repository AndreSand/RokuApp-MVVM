package com.android.rokuapp.data.repository

import com.android.rokuapp.data.model.App
import com.android.rokuapp.data.network.RockuApi

class Repository(private val api: RockuApi) {
    suspend fun getApps(): List<App> {
        return api.getApps().apps
    }
}