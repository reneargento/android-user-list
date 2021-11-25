package com.random.user.presentation.custom

import android.content.Context
import com.random.user.R
import javax.inject.Inject

class StringResources @Inject constructor(
    private val context: Context
) {
    fun getUserFetchErrorMessage() = context.getString(R.string.user_fetch_error)
}