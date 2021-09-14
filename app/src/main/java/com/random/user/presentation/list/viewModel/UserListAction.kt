package com.random.user.presentation.list.viewModel

import android.os.Bundle
import com.random.user.presentation.list.UserView

sealed class UserListAction {
    class DeleteUser(val users: List<UserView>) : UserListAction()

    class Navigate(val bundle: Bundle) : UserListAction()
}