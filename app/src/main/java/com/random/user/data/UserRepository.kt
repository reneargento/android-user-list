package com.random.user.data

import android.content.Context
import com.random.user.R
import com.random.user.data.model.UserListEntity
import com.random.user.data.model.UserFetchError
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userNetwork: UserNetwork,
    private val userDao: UserDao,
    @ApplicationContext private val context: Context
) {
    suspend fun fetchNewUsers(numberOfUsers: Int) : UserListEntity {
        try {
            return withTimeout(5000) {
                userNetwork.fetchUsers(numberOfUsers)
            }
        } catch (error: Exception) {
            throw UserFetchError(context.getString(R.string.user_fetch_error), error)
        }
    }

    suspend fun queryLocalUsers() = userDao.queryAllUsers()

    suspend fun deleteUser(email: String) {
        userDao.deleteUser(email)
    }

    fun queryUsersWithFilter(filter: String): List<User>? =
        userDao.queryUsersWithFilter(filter)
}