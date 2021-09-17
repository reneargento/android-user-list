package com.random.user.presentation.userDetails.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UserDetailsViewModel : ViewModel() {

    private val actionMutableLiveData = MutableLiveData<UserDetailsAction>()
    val actionLiveData = actionMutableLiveData

    fun initView(genderText: String) {
        if (genderText == "Male") {
            actionLiveData.value = UserDetailsAction.RenderMaleIcon
        } else {
            actionLiveData.value = UserDetailsAction.RenderFemaleIcon
        }
    }
}