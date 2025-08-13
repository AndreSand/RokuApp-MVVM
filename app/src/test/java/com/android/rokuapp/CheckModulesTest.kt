package com.android.rokuapp.di

import com.android.rokuapp.di.appModule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.verify.verify

class CheckModulesTest : KoinTest {
    @Test
    fun `verify koin configuration`() {
        appModule.verify()
    }
}
