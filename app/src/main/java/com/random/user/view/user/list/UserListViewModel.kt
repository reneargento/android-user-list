package com.random.user.view.user.list

import androidx.lifecycle.*
import com.random.user.domain.User
import com.random.user.domain.UserDataStore
import com.random.user.domain.UserRepository
import com.random.user.model.UserFetchError
import com.random.user.util.viewModelFactory
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserListViewModel(private val repository: UserRepository,
                        private val userDataStore: UserDataStore,
                        private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
                        ) : ViewModel() {
    companion object {
        val FACTORY = viewModelFactory(::UserListViewModel)
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