package com.random.user.domain.useCase

import com.random.user.domain.UserRepository
import javax.inject.Inject

class FetchNewUsersUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend fun execute(numberOfUsers: Int) = userRepository.fetchNewUsers(numberOfUsers)
}