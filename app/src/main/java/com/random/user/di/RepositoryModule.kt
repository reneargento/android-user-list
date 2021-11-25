package com.random.user.di

import com.random.user.data.UserRepositoryImpl
import com.random.user.domain.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindRepository(repositoryImpl: UserRepositoryImpl): UserRepository
}