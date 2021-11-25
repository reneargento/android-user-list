package com.random.user.data.mapper

import com.random.user.data.User
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class UserDaoToUserDomainMapperTest {

    private lateinit var userDaoToUserDomainMapper: UserDaoToUserDomainMapper

    @Before
    fun onSetup() {
        userDaoToUserDomainMapper = UserDaoToUserDomainMapper()
    }

    @Test
    fun `when user dao is received then it is mapped to an user domain correctly`() {
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

        val user = User(email, gender, name, surname, street, city, state, registered, phone,
            pictureLarge, pictureMedium)

        // when
        val userDomain = userDaoToUserDomainMapper.userDaoToUserDomain(user)

        // then
        with (userDomain) {
            assertEquals(email, this.email)
            assertEquals("Rene Argento", this.fullName)
            assertEquals("Street City State", this.address)
            assertEquals("Male", this.gender)
            assertEquals("2021-08-31", this.registered)
            assertEquals(phone, this.phone)
            assertEquals(pictureLarge, this.pictureLarge)
            assertEquals(pictureMedium, this.pictureMedium)
        }
    }
}