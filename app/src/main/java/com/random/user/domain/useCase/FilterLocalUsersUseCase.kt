package com.random.user.domain.useCase

import com.random.user.domain.UserRepository
import javax.inject.Inject

class FilterLocalUsersUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    fun execute(filter: String) = userRepository.queryUsersWithFilter(filter)
}