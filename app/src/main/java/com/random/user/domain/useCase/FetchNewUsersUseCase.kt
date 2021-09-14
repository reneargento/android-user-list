package com.random.user.domain.useCase

import com.random.user.data.UserDao
import com.random.user.data.UserDataStore
import com.random.user.data.UserRepository
import com.random.user.data.mapper.UserEntityToDaoMapper
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class FetchNewUsersUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val userDataStore: UserDataStore,
    private val userDao: UserDao,
    private val userEntityToDaoMapper: UserEntityToDaoMapper
) {

    suspend fun execute(numberOfUsers: Int) {
        val newUsers = userRepository.fetchNewUsers(numberOfUsers).results.map {
            userEntityToDaoMapper.userEntityToDao(it)
        }
        val deletedUsers = userDataStore.deletedUsersFlow.first()
        val filteredUsersList = newUsers.filterNot { user ->
            deletedUsers.contains(user.email)
        }
        userDao.insertUsers(filteredUsersList)
    }
}