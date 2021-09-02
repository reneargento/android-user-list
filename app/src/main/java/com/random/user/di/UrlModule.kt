package com.random.user.di

import com.random.user.domain.Url
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class UrlModule {

    @Provides
    fun provideUrl() = Url("https://api.randomuser.me/")
}