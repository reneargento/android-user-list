package com.random.user.presentation.list.model

data class UserView(
    val email: String,
    val gender: String,
    val fullName: String,
    val address: String,
    val registered: String,
    val phone: String,
    val pictureLarge: String,
    val pictureMedium: String
)