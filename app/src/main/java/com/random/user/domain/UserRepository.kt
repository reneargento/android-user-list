package com.random.user.domain

import androidx.lifecycle.LiveData
import com.random.user.model.UserFetchError
import com.random.user.model.UserFilterRequest
import kotlinx.coroutines.withTimeout

class UserRepository(
    private val userNetwork: UserNetwork,
    private val userDao: UserDao,
    private val userMapper: UserEntityToDaoMapper = UserEntityToDaoMapper()
) {
    val userLiveData: LiveData<List<User>?> = userDao.userLiveData

    suspend fun queryUsers(numberOfUsers: Int) {
        try {
            val usersList = withTimeout(5000) {
                userNetwork.fetchUsers(numberOfUsers)
            }

            userDao.insertUsers(usersList.results.map { userMapper.userEntityToDao(it) } )
        } catch (error: Exception) {
            throw UserFetchError("Unable to refresh vehicles", error)
        }
    }

    fun usersFilterLiveData(userFilterRequest: UserFilterRequest): LiveData<List<User>?> =
        userDao.userFilterLiveData(userFilterRequest.name,
            userFilterRequest.surname,
            userFilterRequest.email
        )
}