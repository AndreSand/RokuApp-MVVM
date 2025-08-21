package com.android.rokuapp.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class App(
    val id: String,
    val imageUrl: String,
    val name: String
)

@Serializable
data class ApiResponse(
    val apps: List<App> = emptyList()
)