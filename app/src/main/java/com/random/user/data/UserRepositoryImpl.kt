package com.random.user.data

import com.random.user.data.mapper.UserDaoToUserDomainMapper
import com.random.user.data.mapper.UserEntityToDaoMapper
import com.random.user.data.model.UserFetchError
import com.random.user.domain.UserRepository
import com.random.user.domain.model.UserDomain
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userNetwork: UserNetwork,
    private val userDao: UserDao,
    private val userDataStore: UserDataStore,
    private val userEntityToDaoMapper: UserEntityToDaoMapper,
    private val userDaoToUserDomainMapper: UserDaoToUserDomainMapper
): UserRepository {
    override suspend fun fetchNewUsers(numberOfUsers: Int) {
        try {
            val newUsers = withTimeout(5000) {
                userNetwork.fetchUsers(numberOfUsers)
            }.results.map {
                userEntityToDaoMapper.userEntityToDao(it)
            }
            val deletedUsers = userDataStore.deletedUsersFlow.first()
            val filteredUsersList = newUsers.filterNot { user ->
                deletedUsers.contains(user.email)
            }
            userDao.insertUsers(filteredUsersList)
        } catch (error: Exception) {
            throw UserFetchError()
        }
    }

    override suspend fun queryLocalUsers() = userDao.queryAllUsers()?.map {
        userDaoToUserDomainMapper.userDaoToUserDomain(it)
    }

    override suspend fun deleteUser(email: String) {
        userDao.deleteUser(email)
        userDataStore.updateDeletedUsers(email)
    }

    override fun queryUsersWithFilter(filter: String): List<UserDomain>? =
        userDao.queryUsersWithFilter(filter)?.map {
            userDaoToUserDomainMapper.userDaoToUserDomain(it)
        }
}