package com.android.rokuapp.di

import com.android.rokuapp.data.network.ApiService
import com.android.rokuapp.data.network.RockuApi
import com.android.rokuapp.data.repository.Repository
import com.android.rokuapp.viewmodel.AppViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appModule = module {
    // Network Module
    single<RockuApi> { ApiService.api }
    
    // Repository Module
    singleOf(::Repository)
    
    // ViewModel Module
    viewModelOf(::AppViewModel)
}
