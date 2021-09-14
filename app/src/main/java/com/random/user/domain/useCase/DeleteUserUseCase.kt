package com.random.user.domain.useCase

import com.random.user.data.UserDataStore
import com.random.user.data.UserRepository
import javax.inject.Inject

class DeleteUserUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val userDataStore: UserDataStore
) {
    suspend fun execute(email: String) {
        userRepository.deleteUser(email)
        userDataStore.updateDeletedUsers(email)
    }
}