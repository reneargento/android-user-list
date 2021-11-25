package com.random.user.domain.useCase

import com.random.user.domain.UserRepository
import javax.inject.Inject

class QueryLocalUsersUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend fun execute() = userRepository.queryLocalUsers()
}