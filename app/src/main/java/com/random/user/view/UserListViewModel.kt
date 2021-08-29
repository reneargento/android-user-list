package com.random.user.view

import androidx.lifecycle.*
import com.random.user.domain.UserRepository
import com.random.user.model.UserFetchError
import kotlinx.coroutines.launch

class UserListViewModel(private val repository: UserRepository) : ViewModel() {

    companion object {
        val FACTORY = viewModelFactory(::UserListViewModel)
        const val USER_LIST_NUMBER = 6
    }

    val userLiveData = repository.userLiveData

    private val spinner = MutableLiveData(false)
    val spinnerLiveData: LiveData<Boolean>
        get() = spinner

    private val snackBar = MutableLiveData<String?>()
    val snackBarLiveData: LiveData<String?>
        get() = snackBar

    fun fetchUsers() = loadUsers {
        repository.queryUsers(USER_LIST_NUMBER)
    }

    fun onSnackBarShown() {
        snackBar.value = null
    }

    private fun loadUsers(block: suspend () -> Unit) {
        viewModelScope.launch {
            try {
                spinner.value = true
                block()
            } catch (error: UserFetchError) {
                snackBar.value = error.message
            } finally {
                spinner.value = false
            }
        }
    }
}

fun <T : ViewModel, A> viewModelFactory(constructor: (A) -> T):
            (A) -> ViewModelProvider.NewInstanceFactory {
    return { arg: A ->
        object : ViewModelProvider.NewInstanceFactory() {
            @Suppress("UNCHECKED_CAST")
            override fun <V : ViewModel> create(modelClass: Class<V>): V {
                return constructor(arg) as V
            }
        }
    }
}