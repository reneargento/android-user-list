package com.random.user.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.random.user.presentation.userDetails.viewModel.UserDetailsAction
import com.random.user.presentation.userDetails.viewModel.UserDetailsViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class UserDetailsViewModelTest {

    private lateinit var userDetailsViewModel: UserDetailsViewModel

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun onSetup() {
        userDetailsViewModel = UserDetailsViewModel()
    }

    @Test
    fun `when gender icon is computed and gender is male then RenderMaleIcon is emitted`() {
        // given
        val genderText = "Male"

        // when
        userDetailsViewModel.computeGenderIcon(genderText)

        // then
        assert(userDetailsViewModel.actionLiveData.value is UserDetailsAction.RenderMaleIcon)
    }

    @Test
    fun `when gender icon is computed and gender is female then RenderFemaleIcon is emitted`() {
        // given
        val genderText = "Female"

        // when
        userDetailsViewModel.computeGenderIcon(genderText)

        // then
        assert(userDetailsViewModel.actionLiveData.value is UserDetailsAction.RenderFemaleIcon)
    }
}