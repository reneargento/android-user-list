package com.random.user.data

import com.random.user.data.model.UserListEntity
import retrofit2.http.GET
import retrofit2.http.Query

interface UserNetwork {
    @GET("?")
    suspend fun fetchUsers(@Query("results") numberOfUsers: Int): UserListEntity
}