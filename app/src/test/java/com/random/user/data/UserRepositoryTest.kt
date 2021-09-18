package com.random.user.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.mock
import com.random.user.CoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mockito

@ExperimentalCoroutinesApi
class UserRepositoryTest {

    private val mockUserNetwork: UserNetwork = mock()

    private val mockUserDao: UserDao = mock()

    private lateinit var userRepository: UserRepository

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineRule = CoroutineRule()

    @Before
    fun onSetup() {
        userRepository = UserRepository(
            mockUserNetwork,
            mockUserDao
        )
    }

    @Test
    fun `when users are queried then the network is queried`() = coroutineRule.runBlockingTest {
        // given
        val numberOfUsers = 4

        // when
        userRepository.fetchNewUsers(numberOfUsers)

        // then
        Mockito.verify(mockUserNetwork).fetchUsers(numberOfUsers)
    }

    @Test
    fun `when local users are queried then the database is queried`() = coroutineRule.runBlockingTest {
        // when
        userRepository.queryLocalUsers()

        // then
        Mockito.verify(mockUserDao).queryAllUsers()
    }

    @Test
    fun `when user is deleted then the database is updated`() = coroutineRule.runBlockingTest {
        // given
        val email = "email"

        // when
        userRepository.deleteUser(email)

        // then
        Mockito.verify(mockUserDao).deleteUser(email)
    }

    @Test
    fun `when users are filtered then the database is queried`() = coroutineRule.runBlockingTest {
        // given
        val filter = "filter"

        // when
        userRepository.queryUsersWithFilter(filter)

        // then
        Mockito.verify(mockUserDao).queryUsersWithFilter(filter)
    }
}