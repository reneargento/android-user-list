package com.random.user.model

data class UserEntity(
    val email: String,
    val gender: String,
    val name: NameEntity,
    val location: LocationEntity,
    val registered: RegisteredEntity,
    val phone: String,
    val picture: PictureEntity
)