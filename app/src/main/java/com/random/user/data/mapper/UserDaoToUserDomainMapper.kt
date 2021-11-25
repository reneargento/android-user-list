package com.random.user.data.mapper

import com.random.user.data.User
import com.random.user.domain.model.UserDomain
import java.util.*
import javax.inject.Inject

class UserDaoToUserDomainMapper @Inject constructor() {

    fun userDaoToUserDomain(userDao: User) = UserDomain(
        userDao.email,
        userDao.gender.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
        computeFullName(userDao.name, userDao.surname),
        computeAddress(userDao.street, userDao.city, userDao.state),
        userDao.registered.substring(0, userDao.registered.indexOf('T')),
        userDao.phone,
        userDao.pictureLarge,
        userDao.pictureMedium
    )

    private fun computeFullName(name: String, surname: String) = "$name $surname"

    private fun computeAddress(street: String, city: String, state: String) = "$street $city $state"
}