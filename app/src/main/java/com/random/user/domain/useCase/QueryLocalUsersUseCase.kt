package com.random.user.domain.useCase

import com.random.user.data.UserRepository
import com.random.user.domain.mapper.UserDaoToUserDomainMapper
import javax.inject.Inject

class QueryLocalUsersUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val userDaoToUserDomainMapper: UserDaoToUserDomainMapper
) {

    suspend fun execute() = userRepository.queryLocalUsers()?.map {
        userDaoToUserDomainMapper.userDaoToUserDomain(it)
    }
}