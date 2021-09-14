package com.random.user.domain.mapper

import com.random.user.domain.model.UserDomain
import com.random.user.presentation.list.UserView
import javax.inject.Inject

class UserDomainToUserViewMapper @Inject constructor() {

    fun userDomainToUserViewMapper(userDomain: UserDomain) = UserView(
        userDomain.email,
        userDomain.gender,
        userDomain.fullName,
        userDomain.address,
        userDomain.registered,
        userDomain.phone,
        userDomain.pictureLarge,
        userDomain.pictureMedium
    )
}