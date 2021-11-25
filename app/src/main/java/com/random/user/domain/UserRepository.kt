package com.random.user.domain

import com.random.user.domain.model.UserDomain

interface UserRepository {
    suspend fun deleteUser(email: String)
    suspend fun fetchNewUsers(numberOfUsers: Int)
    fun queryUsersWithFilter(filter: String): List<UserDomain>?
    suspend fun queryLocalUsers(): List<UserDomain>?
}