package com.random.user.presentation.list

import com.random.user.data.User
import java.util.*

class UserDaoToViewMapper {

    fun userDaoToView(user: User) = UserView(
        user.email,
        user.gender.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
        computeFullName(user.name, user.surname),
        computeAddress(user.street, user.city, user.state),
        user.registered.substring(0, user.registered.indexOf('T')),
        user.phone,
        user.pictureLarge,
        user.pictureMedium
    )

    private fun computeFullName(name: String, surname: String) = "$name $surname"

    private fun computeAddress(street: String, city: String, state: String) = "$street $city $state"
}