package com.random.user.domain.useCase

import com.random.user.data.UserRepository
import com.random.user.domain.mapper.UserDaoToUserDomainMapper
import javax.inject.Inject

class FilterLocalUsersUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val userDaoToUserDomainMapper: UserDaoToUserDomainMapper
) {

    fun execute(filter: String) = userRepository.queryUsersWithFilter(filter)?.map {
        userDaoToUserDomainMapper.userDaoToUserDomain(it)
    }
}