package com.random.user.domain.useCase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.*
import com.random.user.CoroutineRule
import com.random.user.data.UserDataStore
import com.random.user.data.UserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

@ExperimentalCoroutinesApi
class DeleteUserUseCaseTest {

    private val mockUserRepository: UserRepository = mock()

    private val mockUserDataStore: UserDataStore = mock()

    private lateinit var deleteUserUseCase: DeleteUserUseCase

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineRule = CoroutineRule()

    @Before
    fun onSetup() {
        deleteUserUseCase = DeleteUserUseCase(mockUserRepository, mockUserDataStore)
    }

    @Test
    fun `when delete use case is executed then repository and data store are updated`()
            = coroutineRule.runBlockingTest {
        // given
        val email = "r.a@email.com"

        // when
        deleteUserUseCase.execute(email)

        // then
        verify(mockUserRepository).deleteUser(email)
        verify(mockUserDataStore).updateDeletedUsers(email)
    }
}