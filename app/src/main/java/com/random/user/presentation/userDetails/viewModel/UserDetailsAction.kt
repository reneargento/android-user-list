package com.random.user.presentation.userDetails.viewModel

sealed class UserDetailsAction {

    object RenderMaleIcon : UserDetailsAction()

    object RenderFemaleIcon : UserDetailsAction()
}