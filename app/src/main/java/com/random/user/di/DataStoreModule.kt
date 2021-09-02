package com.random.user.di

import android.content.Context
import com.random.user.domain.UserDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DataStoreModule {

    @Provides
    fun provideUserDataStore(@ApplicationContext context: Context) =
        UserDataStore(context)
}