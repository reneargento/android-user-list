package com.random.user.domain.mapper

import com.random.user.domain.model.UserDomain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class UserDomainToUserViewMapperTest {

    private lateinit var userDomainToUserViewMapper: UserDomainToUserViewMapper

    @Before
    fun onSetup() {
        userDomainToUserViewMapper = UserDomainToUserViewMapper()
    }

    @Test
    fun `when user domain is received then it is mapped to an user view correctly`() {
        // given
        val email = "r.a@email.com"
        val gender = "Male"
        val fullName = "Rene Argento"
        val address = "Street City State"
        val registered = "2021-08-31"
        val phone = "12345678"
        val pictureLarge = "picture1"
        val pictureMedium = "picture2"

        val user = UserDomain(email, gender, fullName, address, registered, phone,
            pictureLarge, pictureMedium)

        // when
        val userView = userDomainToUserViewMapper.userDomainToUserViewMapper(user)

        // then
        with (userView) {
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