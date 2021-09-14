package com.random.user.data

import com.random.user.data.model.UserListEntity
import com.random.user.domain.UserFetchError
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userNetwork: UserNetwork,
    private val userDao: UserDao
) {
    suspend fun fetchNewUsers(numberOfUsers: Int) : UserListEntity {
        try {
            return withTimeout(5000) {
                userNetwork.fetchUsers(numberOfUsers)
            }
        } catch (error: Exception) {
            throw UserFetchError("Unable to refresh vehicles", error)
        }
    }

    suspend fun queryLocalUsers() = userDao.queryAllUsers()

    suspend fun deleteUser(email: String) {
        userDao.deleteUser(email)
    }

    fun queryUsersWithFilter(filter: String): List<User>? =
        userDao.queryUsersWithFilter(filter)
}