package com.random.user.di

import android.content.Context
import com.random.user.domain.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object UserModule {

    @Provides
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
    fun provideUserNetwork(
        retrofit: Retrofit
    ): UserNetwork = retrofit.create(UserNetwork::class.java)

    @Provides
    fun provideUserDao(@ApplicationContext context: Context) = getDatabase(context).userDao

    @Provides
    fun provideCoroutineDispatcher() = Dispatchers.IO
}