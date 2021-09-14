package com.random.user.presentation.list.viewModel

import android.text.Editable
import androidx.core.os.bundleOf
import androidx.lifecycle.*
import com.random.user.domain.UserFetchError
import com.random.user.domain.mapper.UserDomainToUserViewMapper
import com.random.user.domain.useCase.DeleteUserUseCase
import com.random.user.domain.useCase.FetchNewUsersUseCase
import com.random.user.domain.useCase.FilterLocalUsersUseCase
import com.random.user.domain.useCase.QueryLocalUsersUseCase
import com.random.user.presentation.list.model.UserView
import com.random.user.presentation.userDetails.UserDetailsFragment
import com.random.user.util.SingleLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val fetchNewUsersUseCase: FetchNewUsersUseCase,
    private val queryLocalUsersUseCase: QueryLocalUsersUseCase,
    private val filterLocalUsersUseCase: FilterLocalUsersUseCase,
    private val deleteUserUseCase: DeleteUserUseCase,
    private val userDomainToUserViewMapper: UserDomainToUserViewMapper,
    private val coroutineDispatcher: CoroutineDispatcher
) : ViewModel() {

    companion object {
        const val USER_LIST_NUMBER = 4
    }

    private val userListViewState: MutableLiveData<UserListViewState> = MutableLiveData()
    private val userListAction: SingleLiveData<UserListAction> = SingleLiveData()

    val userListViewStateLiveData: LiveData<UserListViewState> = userListViewState
    val userListActionLiveData: LiveData<UserListAction> = userListAction

    var isRequestingUsers = false

    init {
        userListViewState.value = UserListViewState.Initial
    }

    fun onScroll(searchText: Editable?, totalItemCount: Int, lastVisibleItemPosition: Int) {
        if (!isRequestingUsers
            && searchText.isNullOrEmpty()
            && totalItemCount == lastVisibleItemPosition + 1) {
            fetchUsers()
        }
    }

    fun fetchUsers() = loadUsers {
        isRequestingUsers = true
        fetchNewUsersUseCase.execute(USER_LIST_NUMBER)

        val userList = queryLocalUsers()
        userListViewState.postValue(UserListViewState.Results(userList))
    }

    fun deleteUser(email: String) {
        viewModelScope.launch {
            withContext(coroutineDispatcher) {
                deleteUserUseCase.execute(email)
                val userList = queryLocalUsers()
                userListAction.postValue(UserListAction.DeleteUser(userList))
            }
        }
    }

    private fun loadUsers(loadUsersBlock: suspend () -> Unit) {
        viewModelScope.launch {
            try {
                withContext(coroutineDispatcher) {
                    loadUsersBlock()
                }
            } catch (error: UserFetchError) {
                userListViewState.value = UserListViewState.Error(error.message)
            } finally {
                isRequestingUsers = false
            }
        }
    }

    private suspend fun queryLocalUsers() = queryLocalUsersUseCase.execute()?.map {
        userDomainToUserViewMapper.userDomainToUserViewMapper(it)
    } ?: emptyList()

    fun onUserClicked(user: UserView) {
        val bundle = bundleOf(
            UserDetailsFragment.NAME_PARAM to user.fullName,
            UserDetailsFragment.GENDER_PARAM to user.gender,
            UserDetailsFragment.ADDRESS_PARAM to user.address,
            UserDetailsFragment.REGISTERED_DATE_PARAM to user.registered,
            UserDetailsFragment.EMAIL_PARAM to user.email,
            UserDetailsFragment.PICTURE_PARAM to user.pictureLarge,
        )
        userListAction.value = UserListAction.Navigate(bundle)
    }

    fun filterUsers(filter: String) {
        viewModelScope.launch {
            withContext(coroutineDispatcher) {
                val filteredUserList = filterLocalUsersUseCase.execute(filter)?.map {
                    userDomainToUserViewMapper.userDomainToUserViewMapper(it)
                } ?: emptyList()
                userListViewState.postValue(UserListViewState.Results(filteredUserList))
            }
        }
    }
}