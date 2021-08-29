package com.random.user.view

import com.random.user.domain.User

class UserDaoToViewMapper {
    fun userDaoToView(user: User) = UserView(
        user.email,
        user.gender,
        user.name,
        user.surname,
        user.street,
        user.city,
        user.state,
        user.registered,
        user.phone,
        user.pictureLarge,
        user.pictureThumbnail
    )
}