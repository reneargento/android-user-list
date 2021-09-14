package com.random.user.presentation.list.viewModel

import com.random.user.presentation.list.model.UserView

sealed class UserListViewState {
    object Initial : UserListViewState()

    object Loading : UserListViewState()

    class Results(val users: List<UserView>) : UserListViewState()

    class Error(val errorMessage: String?) : UserListViewState()
}