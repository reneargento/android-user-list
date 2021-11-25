package com.random.user.domain.useCase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.*
import com.random.user.CoroutineRule
import com.random.user.domain.UserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

@ExperimentalCoroutinesApi
class FetchNewUsersUseCaseTest {

    private val mockUserRepository: UserRepository = mock()

    private lateinit var fetchNewUsersUseCase: FetchNewUsersUseCase

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineRule = CoroutineRule()

    @Before
    fun onSetup() {
        fetchNewUsersUseCase = FetchNewUsersUseCase(
            mockUserRepository
        )
    }

    @Test
    fun `when fetch new users use case is executed then users are fetched in the repository`()
            = coroutineRule.runBlockingTest {
        // given
        val numberOfUsers = 4

        // when
        fetchNewUsersUseCase.execute(numberOfUsers)

        // then
        verify(mockUserRepository).fetchNewUsers(numberOfUsers)
    }
}