package com.random.user.data.mapper

import com.random.user.data.User
import com.random.user.data.model.*
import javax.inject.Inject

class UserDaoToEntityMapper @Inject constructor() {
    fun userDaoToEntity(user: User) = UserEntity(
        user.email,
        user.gender,
        NameEntity(user.name, user.surname),
        LocationEntity(computeStreetEntity(user.street), user.city, user.state),
        RegisteredEntity(user.registered),
        user.phone,
        PictureEntity(user.pictureLarge, user.pictureMedium)
    )

    private fun computeStreetEntity(street : String) : StreetEntity {
        val streetComponents = street.split(" ")
        return StreetEntity(streetComponents[0].toInt(), streetComponents[1])
    }
}