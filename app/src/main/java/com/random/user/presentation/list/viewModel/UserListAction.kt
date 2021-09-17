package com.random.user.presentation.list.viewModel

import android.os.Bundle
import android.widget.ImageView
import com.random.user.presentation.list.model.UserView

sealed class UserListAction {
    class DeleteUser(val users: List<UserView>) : UserListAction()

    class Navigate(val bundle: Bundle, val imageView: ImageView) : UserListAction()
}