package com.random.user.data.mapper

import com.random.user.data.model.*
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test

class UserEntityToDaoMapperTest {

    private lateinit var userEntityToDaoMapper: UserEntityToDaoMapper

    @Before
    fun onSetup() {
        userEntityToDaoMapper = UserEntityToDaoMapper()
    }

    @Test
    fun `when user entity is received then it is mapped to a dao model correctly`() {
        // given
        val email = "r.a@email.com"
        val gender = "male"
        val name = "Rene"
        val surname = "Argento"
        val street = "Street"
        val city = "City"
        val state = "State"
        val registered = "2021-08-31T02:42:55.697Z"
        val phone = "12345678"
        val pictureLarge = "picture1"
        val pictureMedium = "picture2"

        val nameEntity = NameEntity(name, surname)
        val streetEntity = StreetEntity(12, street)
        val locationEntity = LocationEntity(streetEntity, city, state)
        val registeredEntity = RegisteredEntity(registered)
        val pictureEntity = PictureEntity(pictureLarge, pictureMedium)

        val userEntity = UserEntity(email, gender, nameEntity, locationEntity, registeredEntity, phone,
            pictureEntity)

        // when
        val userDao = userEntityToDaoMapper.userEntityToDao(userEntity)

        // then
        with (userDao) {
            assertEquals(email, this.email)
            assertEquals(gender, this.gender)
            assertEquals(name, this.name)
            assertEquals(surname, this.surname)
            assertEquals("12 $street", this.street)
            assertEquals(city, this.city)
            assertEquals(state, this.state)
            assertEquals(registered, this.registered)
            assertEquals(phone, this.phone)
            assertEquals(pictureLarge, this.pictureLarge)
            assertEquals(pictureMedium, this.pictureMedium)
        }
    }
}