package com.random.user.domain.useCase

import com.random.user.domain.UserRepository
import javax.inject.Inject

class DeleteUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend fun execute(email: String) = userRepository.deleteUser(email)
}