package com.random.user.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.*
import com.random.user.CoroutineRule
import com.random.user.data.model.UserFetchError
import com.random.user.presentation.list.mapper.UserDomainToUserViewMapper
import com.random.user.domain.useCase.DeleteUserUseCase
import com.random.user.domain.useCase.FetchNewUsersUseCase
import com.random.user.domain.useCase.FilterLocalUsersUseCase
import com.random.user.domain.useCase.QueryLocalUsersUseCase
import com.random.user.presentation.list.model.UserView
import com.random.user.presentation.list.viewModel.UserListAction
import com.random.user.presentation.list.viewModel.UserListViewModel
import com.random.user.presentation.list.viewModel.UserListViewState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

@ExperimentalCoroutinesApi
class UserListViewModelTest {

    private val mockFetchNewUsersUseCase: FetchNewUsersUseCase = mock()

    private val mockQueryLocalUsersUseCase: QueryLocalUsersUseCase = mock()

    private val mockFilterLocalUsersUseCase: FilterLocalUsersUseCase = mock()

    private val mockDeleteUserUseCase: DeleteUserUseCase = mock()

    private val mockUserDomainToUserViewMapper: UserDomainToUserViewMapper = mock()

    private lateinit var userListViewModel: UserListViewModel

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineRule = CoroutineRule()

    @Before
    fun onSetup() {
        userListViewModel = UserListViewModel(
            mockFetchNewUsersUseCase,
            mockQueryLocalUsersUseCase,
            mockFilterLocalUsersUseCase,
            mockDeleteUserUseCase,
            mockUserDomainToUserViewMapper,
            TestCoroutineDispatcher()
        )
    }

    @Test
    fun `when scrolling and filter is empty and last item is reached then load more users`()
            = coroutineRule.runBlockingTest {
        // given
        val searchText = ""
        val totalItemCount = 10
        val lastVisibleItemPosition = 9

        // when
        userListViewModel.onScroll(searchText, totalItemCount, lastVisibleItemPosition)

        // then
        verify(mockFetchNewUsersUseCase, times(2)).execute(UserListViewModel.USER_LIST_NUMBER)
    }

    @Test
    fun `when scrolling and filter is not empty then do not load more users`()
            = coroutineRule.runBlockingTest {
        // given
        val searchText = "filter"
        val totalItemCount = 10
        val lastVisibleItemPosition = 9

        // when
        userListViewModel.onScroll(searchText, totalItemCount, lastVisibleItemPosition)

        // then
        verify(mockFetchNewUsersUseCase, times(1)).execute(UserListViewModel.USER_LIST_NUMBER)
    }

    @Test
    fun `when scrolling and last item is not reached then do not load more users`()
            = coroutineRule.runBlockingTest {
        // given
        val searchText = ""
        val totalItemCount = 10
        val lastVisibleItemPosition = 5

        // when
        userListViewModel.onScroll(searchText, totalItemCount, lastVisibleItemPosition)

        // then
        verify(mockFetchNewUsersUseCase, times(1)).execute(UserListViewModel.USER_LIST_NUMBER)
    }

    @Test
    fun `when loading users then new users are fetched, database is queried and view state is results`()
            = coroutineRule.runBlockingTest {
        // when
        userListViewModel.loadUsers()

        // then
        verify(mockFetchNewUsersUseCase, times(2)).execute(UserListViewModel.USER_LIST_NUMBER)
        verify(mockQueryLocalUsersUseCase, times(2)).execute()
        assert(userListViewModel.userListViewStateLiveData.value is UserListViewState.Results)
    }

    @Test
    fun `when error occurs during users load then view state is error`()
            = coroutineRule.runBlockingTest {
        // given
        whenever(mockFetchNewUsersUseCase.execute(UserListViewModel.USER_LIST_NUMBER))
            .thenThrow(UserFetchError("message", RuntimeException()))

        // when
        userListViewModel.loadUsers()

        // then
        assert(userListViewModel.userListViewStateLiveData.value is UserListViewState.Error)
    }

    @Test
    fun `when user is deleted and filter is empty then delete use case is executed and local users are queried`()
            = coroutineRule.runBlockingTest {
        // given
        val email = "r.a@email.com"
        userListViewModel.filterApplied = ""

        // when
        userListViewModel.deleteUser(email)

        // then
        verify(mockQueryLocalUsersUseCase, times(2)).execute()
        verify(mockFilterLocalUsersUseCase, never()).execute(userListViewModel.filterApplied)
        assert(userListViewModel.userListActionLiveData.value is UserListAction.DeleteUser)
    }

    @Test
    fun `when user is deleted and filter is not empty then delete use case is executed and users are filtered`()
            = coroutineRule.runBlockingTest {
        // given
        val email = "r.a@email.com"
        userListViewModel.filterApplied = "rene"

        // when
        userListViewModel.deleteUser(email)

        // then
        verify(mockQueryLocalUsersUseCase, times(1)).execute()
        verify(mockFilterLocalUsersUseCase, times(1)).execute(userListViewModel.filterApplied)
        assert(userListViewModel.userListActionLiveData.value is UserListAction.DeleteUser)
    }

    @Test
    fun `when user is clicked then action is to navigate`() {
        // given
        val userView = givenUserView()

        // when
        userListViewModel.onUserClicked(userView, mock())

        // then
        assert(userListViewModel.userListActionLiveData.value is UserListAction.Navigate)
    }

    @Test
    fun `when users are filtered then filter use case is executed and view state is results`() {
        // given
        val filter = "Rene"

        // when
        userListViewModel.filterUsers(filter)

        // then
        assertEquals(filter, userListViewModel.filterApplied)
        verify(mockFilterLocalUsersUseCase, times(1)).execute(filter)
        assert(userListViewModel.userListViewStateLiveData.value is UserListViewState.Results)
    }

    private fun givenUserView() = UserView(
    "r.a@email.com",
    "Male",
    "Rene Argento",
    "Barcelona Spain",
    "2021-04-18",
    "12345678",
    "picture1",
    "picture2"
    )
}