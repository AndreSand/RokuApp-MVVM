package com.android.rokuapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.rokuapp.data.repository.Repository
import com.android.rokuapp.data.model.App
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AppState(
    val apps: List<App> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class AppViewModel : ViewModel() {
    private val _state = MutableStateFlow(AppState())
    val state: StateFlow<AppState> = _state.asStateFlow()

    init {
        fetchApps()
    }

    fun fetchApps() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            try {
                val apps = Repository().getApps()
                _state.value = _state.value.copy(
                    apps = apps,
                    isLoading = false
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "Unknown error occurred"
                )
            }
        }
    }
}
