package com.random.user.mappers

import com.random.user.data.User
import com.random.user.presentation.user.list.UserDaoToViewMapper
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test

class UserDaoToViewMapperUnitTest {

    private lateinit var userDaoToViewMapper: UserDaoToViewMapper

    @Before
    fun onSetup() {
        userDaoToViewMapper = UserDaoToViewMapper()
    }

    @Test
    fun `when user dao is received then it is mapped to a view model correctly`() {
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
        val userView = userDaoToViewMapper.userDaoToView(user)

        // then
        with (userView) {
            assertEquals(this.email, email)
            assertEquals(this.fullName, "Rene Argento")
            assertEquals(this.address, "Street City State")
            assertEquals(this.gender, "Male")
            assertEquals(this.registered, "2021-08-31")
            assertEquals(this.phone, phone)
            assertEquals(this.pictureLarge, pictureLarge)
            assertEquals(this.pictureMedium, pictureMedium)
        }
    }
}