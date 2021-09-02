package com.random.user.domain

import com.random.user.model.UserEntity
import javax.inject.Inject

class UserEntityToDaoMapper @Inject constructor() {
    fun userEntityToDao(userEntity: UserEntity) = User(
        userEntity.email,
        userEntity.gender,
        userEntity.name.first,
        userEntity.name.last,
        computeStreet(userEntity.location.street.number, userEntity.location.street.name),
        userEntity.location.city,
        userEntity.location.state,
        userEntity.registered.date,
        userEntity.phone,
        userEntity.picture.large,
        userEntity.picture.medium
    )

    private fun computeStreet(streetNumber: Int, streetName: String) = "$streetNumber $streetName"
}