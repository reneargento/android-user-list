package com.random.user.data

import androidx.lifecycle.LiveData
import com.random.user.domain.UserEntityToDaoMapper
import com.random.user.domain.UserFetchError
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userNetwork: UserNetwork,
    private val userDao: UserDao,
    private val userMapper: UserEntityToDaoMapper
) {
    val userLiveData: LiveData<List<User>?> = userDao.userLiveData

    suspend fun queryUsers(numberOfUsers: Int, deletedUsers: Set<String>) {
        try {
            val usersList = withTimeout(5000) {
                userNetwork.fetchUsers(numberOfUsers)
            }
            val filteredUsersList = usersList.results.filterNot { user ->
                deletedUsers.contains(user.email)
            }
            userDao.insertUsers(filteredUsersList.map { userMapper.userEntityToDao(it) } )
        } catch (error: Exception) {
            throw UserFetchError("Unable to refresh vehicles", error)
        }
    }

    suspend fun deleteUser(email: String) {
        userDao.deleteUser(email)
    }

    fun usersFilterLiveData(filter: String): LiveData<List<User>?> =
        userDao.userFilterLiveData(filter)
}