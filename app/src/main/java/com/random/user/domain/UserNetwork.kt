package com.random.user.domain

import com.random.user.model.ApiUrl
import com.random.user.model.UserListEntity
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface UserNetwork {
    @GET("?")
    suspend fun fetchUsers(@Query("results") numberOfUsers: Int): UserListEntity
}

fun getNetworkService() = service

private val service: UserNetwork by lazy {
    val okHttpClient = OkHttpClient.Builder()
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl(ApiUrl.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    retrofit.create(UserNetwork::class.java)
}