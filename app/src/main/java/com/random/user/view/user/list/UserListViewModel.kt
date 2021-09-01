package com.random.user.view.user.list

import androidx.lifecycle.*
import com.random.user.domain.User
import com.random.user.domain.UserDataStore
import com.random.user.domain.UserRepository
import com.random.user.model.UserFetchError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val repository: UserRepository,
    private val userDataStore: UserDataStore,
    private val coroutineDispatcher: CoroutineDispatcher
) : ViewModel() {

    companion object {
        const val USER_LIST_NUMBER = 4
    }

    val userLiveData = repository.userLiveData

    private val filteredUsersLiveData: MutableLiveData<String> = MutableLiveData()

    val filteredUsersLiveDataObservable: LiveData<List<User>> =
        Transformations.switchMap(filteredUsersLiveData) { text ->
            repository.usersFilterLiveData(text)
    }

    private val spinner = MutableLiveData(false)
    val spinnerLiveData: LiveData<Boolean>
        get() = spinner

    private val snackBar = MutableLiveData<String?>()
    val snackBarLiveData: LiveData<String?>
        get() = snackBar

    var isRequestingUsers = false

    fun onSnackBarShown() {
        snackBar.value = null
    }

    fun fetchUsers() = loadUsers {
        isRequestingUsers = true
        val deletedUsers = userDataStore.deletedUsersFlow.first()
        repository.queryUsers(USER_LIST_NUMBER, deletedUsers)
    }

    fun deleteUser(email: String) {
        viewModelScope.launch {
            withContext(coroutineDispatcher) {
                repository.deleteUser(email)
            }
            snackBar.value = "User deleted"
            userDataStore.updateDeletedUsers(email)
        }
    }

    private fun loadUsers(loadUsersBlock: suspend () -> Unit) {
        viewModelScope.launch {
            try {
                spinner.value = true
                withContext(coroutineDispatcher) {
                    loadUsersBlock()
                }
            } catch (error: UserFetchError) {
                snackBar.value = error.message
            } finally {
                spinner.value = false
                isRequestingUsers = false
            }
        }
    }

    fun filterUsers(filter: String) {
        filteredUsersLiveData.value = filter
    }
}