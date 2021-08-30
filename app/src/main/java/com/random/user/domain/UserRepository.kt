package com.random.user.domain

import androidx.lifecycle.LiveData
import com.random.user.model.UserFetchError
import kotlinx.coroutines.withTimeout

class UserRepository(
    private val userNetwork: UserNetwork,
    private val userDao: UserDao,
    private val userMapper: UserEntityToDaoMapper = UserEntityToDaoMapper()
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