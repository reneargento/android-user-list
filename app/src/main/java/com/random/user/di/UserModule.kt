package com.random.user.di

import android.content.Context
import androidx.room.Room
import com.random.user.data.*
import com.random.user.presentation.custom.StringResources
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserModule {

    @Provides
    @Singleton
    fun provideRetrofit(
        url: Url
    ) : Retrofit {
        val okHttpClient = OkHttpClient.Builder().build()
        return Retrofit.Builder()
            .baseUrl(url.getUrl())
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideUserNetwork(
        retrofit: Retrofit
    ): UserNetwork = retrofit.create(UserNetwork::class.java)

    @Provides
    @Singleton
    fun provideUserDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
            context.applicationContext,
            UserDatabase::class.java,
            "users_db")
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun provideUserDao(userDatabase: UserDatabase) = userDatabase.userDao

    @Provides
    fun provideCoroutineDispatcher() = Dispatchers.IO

    @Provides
    fun provideStringResources(@ApplicationContext context: Context) = StringResources(context)
}