package com.random.user.di

import android.content.Context
import com.random.user.domain.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
object UserModule {

    @Provides
    fun provideUserNetwork() = getNetworkService()

    @Provides
    fun provideUserDao(@ApplicationContext context: Context) = getDatabase(context).userDao

    @Provides
    fun provideUserEntityToDaoMapper() = UserEntityToDaoMapper()

    @Provides
    fun provideCoroutineDispatcher() = Dispatchers.IO

    @Provides
    fun provideUserRepository(
        userNetwork: UserNetwork,
        userDao: UserDao,
        userMapper: UserEntityToDaoMapper
    ) = UserRepository(userNetwork, userDao, userMapper)
}