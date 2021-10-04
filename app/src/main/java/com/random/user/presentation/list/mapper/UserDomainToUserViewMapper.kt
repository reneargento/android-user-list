package com.random.user.presentation.list.mapper

import com.random.user.domain.model.UserDomain
import com.random.user.presentation.list.model.UserView
import javax.inject.Inject

class UserDomainToUserViewMapper @Inject constructor() {

    fun userDomainToUserViewMapper(user: UserDomain) = UserView(
        user.email,
        user.gender,
        user.fullName,
        user.address,
        user.registered,
        user.phone,
        user.pictureLarge,
        user.pictureMedium
    )
}