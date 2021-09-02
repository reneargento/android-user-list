package com.random.user

import android.content.Context
import com.random.user.di.DataStoreModule
import com.random.user.di.UrlModule
import com.random.user.domain.UserDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [UrlModule::class, DataStoreModule::class]
)
class MockModule {

    @Provides
    @Singleton
    fun provideUrl(): String = "http://localhost:1234/"

    @Provides
    @Singleton
    fun provideUserDataStore(@ApplicationContext context: Context) : UserDataStore =
        MockUserDataStore(context)
}