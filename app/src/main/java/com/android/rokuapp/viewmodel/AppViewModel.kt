package com.android.rokuapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.rokuapp.data.repository.Repository
import com.android.rokuapp.data.model.App
import com.android.rokuapp.ui.utils.ApiResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AppViewModel : ViewModel() {
    private val _state = MutableStateFlow<ApiResponse<List<App>>>(ApiResponse.Loading)
    val state: StateFlow<ApiResponse<List<App>>> = _state.asStateFlow()

    init {
        fetchApps()
    }

    fun fetchApps() {
        viewModelScope.launch {
            _state.value = ApiResponse.Loading
            try {
                val apps = Repository().getApps()
                _state.value = ApiResponse.Success(apps)
            } catch (e: Exception) {
                _state.value = ApiResponse.Error(e.message ?: "Unknown error occurred")
            }
        }
    }
}
