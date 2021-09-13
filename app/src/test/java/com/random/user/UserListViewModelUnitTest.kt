package com.random.user

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.random.user.data.UserDataStore
import com.random.user.data.UserRepository
import com.random.user.presentation.user.list.UserListViewModel
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class UserListViewModelUnitTest {

    private val mockDataStore: UserDataStore = mock()

    private val mockRepository: UserRepository = mock()

    private lateinit var userListViewModel: UserListViewModel

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineRule = CoroutineRule()

    @Before
    fun onSetup() {
        userListViewModel = UserListViewModel(mockRepository, mockDataStore, TestCoroutineDispatcher())
    }

    @Test
    fun `when users are fetched then the repository is queried with deleted users`()
    = coroutineRule.runBlockingTest {
        // given
        val numberOfUsers = UserListViewModel.USER_LIST_NUMBER
        val deletedUsers = setOf("email1", "email2")
        val deletedUsersFlow = flow {
            emit(deletedUsers)
        }
        whenever(mockDataStore.deletedUsersFlow).thenReturn(deletedUsersFlow)

        // when
        userListViewModel.fetchUsers()

        // then
        verify(mockRepository).queryUsers(numberOfUsers, deletedUsers)
    }

    @Test
    fun `when user is deleted then the repository and data store are updated`()
    = coroutineRule.runBlockingTest {
        // given
        val email = "r.a@email.com"

        // when
        userListViewModel.deleteUser(email)

        // then
        verify(mockRepository).deleteUser(email)
        verify(mockDataStore).updateDeletedUsers(email)
    }
}