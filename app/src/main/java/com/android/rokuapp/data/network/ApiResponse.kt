package com.android.rokuapp.data.network

import com.android.rokuapp.data.model.App
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class ApiResponse {

    @SerialName("apps")
    val apps: List<App> = emptyList()
}