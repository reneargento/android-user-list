package com.random.user.presentation.list.viewModel

import android.widget.ImageView
import androidx.annotation.VisibleForTesting
import androidx.core.os.bundleOf
import androidx.lifecycle.*
import com.random.user.data.model.UserFetchError
import com.random.user.presentation.list.mapper.UserDomainToUserViewMapper
import com.random.user.domain.useCase.DeleteUserUseCase
import com.random.user.domain.useCase.FetchNewUsersUseCase
import com.random.user.domain.useCase.FilterLocalUsersUseCase
import com.random.user.domain.useCase.QueryLocalUsersUseCase
import com.random.user.presentation.list.model.UserView
import com.random.user.presentation.userDetails.view.UserDetailsFragment
import com.random.user.presentation.custom.SingleLiveData
import com.random.user.presentation.custom.StringResources
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
    private val coroutineDispatcher: CoroutineDispatcher,
    private val stringResources: StringResources
) : ViewModel() {

    companion object {
        const val USER_LIST_NUMBER = 4
    }

    private val userListViewState: MutableLiveData<UserListViewState> = MutableLiveData()
    val userListViewStateLiveData: LiveData<UserListViewState> = userListViewState

    private val userListAction: SingleLiveData<UserListAction> = SingleLiveData()
    val userListActionLiveData: LiveData<UserListAction> = userListAction

    private var isRequestingUsers = false
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    var filterApplied = ""

    init {
        loadUsers()
    }

    fun onScroll(searchText: String, totalItemCount: Int, lastVisibleItemPosition: Int) {
        if (!isRequestingUsers
            && searchText.isEmpty()
            && totalItemCount == lastVisibleItemPosition + 1) {
            loadUsers()
        }
    }

    fun loadUsers() {
        if (filterApplied.isNotEmpty()) return
        userListViewState.value = UserListViewState.Loading

        viewModelScope.launch {
            try {
                withContext(coroutineDispatcher) {
                    isRequestingUsers = true
                    fetchNewUsersUseCase.execute(USER_LIST_NUMBER)
                }
                val userList = queryLocalUsers()
                userListViewState.value = UserListViewState.Results(userList)
            } catch (error: UserFetchError) {
                userListViewState.value =
                    UserListViewState.Error(stringResources.getUserFetchErrorMessage())
            } finally {
                isRequestingUsers = false
            }
        }
    }

    fun deleteUser(email: String) {
        viewModelScope.launch {
            withContext(coroutineDispatcher) {
                deleteUserUseCase.execute(email)
            }
            val userList =
                if (filterApplied.isEmpty())
                    queryLocalUsers()
                else
                    filterLocalUsers(filterApplied)
            userListAction.value = UserListAction.DeleteUser(userList)
        }
    }

    private suspend fun queryLocalUsers() =
        withContext(coroutineDispatcher) {
            queryLocalUsersUseCase.execute()?.map {
                userDomainToUserViewMapper.userDomainToUserViewMapper(it)
            } ?: emptyList()
        }

    fun onUserClicked(user: UserView, imageView: ImageView) {
        val bundle = bundleOf(
            UserDetailsFragment.NAME_PARAM to user.fullName,
            UserDetailsFragment.GENDER_PARAM to user.gender,
            UserDetailsFragment.ADDRESS_PARAM to user.address,
            UserDetailsFragment.REGISTERED_DATE_PARAM to user.registered,
            UserDetailsFragment.EMAIL_PARAM to user.email,
            UserDetailsFragment.PICTURE_PARAM to user.pictureLarge
        )
        userListAction.value = UserListAction.Navigate(bundle, imageView)
    }

    fun filterUsers(filter: String) {
        filterApplied = filter

        viewModelScope.launch {
            val filteredUserList = filterLocalUsers(filter)
            userListViewState.value = UserListViewState.Results(filteredUserList)
        }
    }

    private suspend fun filterLocalUsers(filter: String) =
        withContext(coroutineDispatcher) {
            filterLocalUsersUseCase.execute(filter)?.map {
                userDomainToUserViewMapper.userDomainToUserViewMapper(it)
            } ?: emptyList()
        }
}