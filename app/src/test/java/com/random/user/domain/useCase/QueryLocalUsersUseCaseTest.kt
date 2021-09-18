package com.random.user.domain.useCase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.*
import com.random.user.CoroutineRule
import com.random.user.data.UserRepository
import com.random.user.domain.mapper.UserDaoToUserDomainMapper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

@ExperimentalCoroutinesApi
class QueryLocalUsersUseCaseTest {

    private val mockUserRepository: UserRepository = mock()

    private val mockUserDaoToUserDomainMapper: UserDaoToUserDomainMapper = mock()

    private lateinit var queryLocalUsersUseCase: QueryLocalUsersUseCase

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineRule = CoroutineRule()

    @Before
    fun onSetup() {
        queryLocalUsersUseCase = QueryLocalUsersUseCase(
            mockUserRepository,
            mockUserDaoToUserDomainMapper
        )
    }

    @Test
    fun `when query users use case is executed then repository is queried`()
            = coroutineRule.runBlockingTest {
        // when
        queryLocalUsersUseCase.execute()

        // then
        verify(mockUserRepository).queryLocalUsers()
    }
}