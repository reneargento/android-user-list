package com.random.user

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import com.random.user.data.User
import com.random.user.data.UserDao
import com.random.user.data.UserNetwork
import com.random.user.data.UserRepository
import com.random.user.data.model.*
import com.random.user.data.mapper.UserEntityToDaoMapper
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mockito

class UserRepositoryUnitTest {

    private val mockUserNetwork: UserNetwork = mock()

    private val mockUserDao: UserDao = mock()

    private val mockUserEntityToDaoMapper: UserEntityToDaoMapper = mock()

    private lateinit var userRepository: UserRepository

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineRule = CoroutineRule()

    @Before
    fun onSetup() {
        userRepository = UserRepository(
            mockUserNetwork,
            mockUserDao,
            mockUserEntityToDaoMapper
        )
    }

    @Test
    fun `when users are queried then the filter is applied and the database is updated`()
    = coroutineRule.runBlockingTest {
        // given
        val numberOfUsers = 4
        val deletedUsers = setOf("email2", "email3")
        val userEntityList = givenUserEntityList()
        val userList = givenUserList(userEntityList.results)
        given(mockUserNetwork.fetchUsers(numberOfUsers)).willReturn(userEntityList)
        for ((index, user) in userEntityList.results.withIndex()) {
            given(mockUserEntityToDaoMapper.userEntityToDao(user))
                .willReturn(userList[index])
        }
        val filteredList = listOf(userList[0], userList[3])

        // when
        userRepository.fetchNewUsers(numberOfUsers, deletedUsers)

        // then
        Mockito.verify(mockUserNetwork).fetchUsers(numberOfUsers)
        Mockito.verify(mockUserDao).insertUsers(filteredList)
    }

    private fun givenUserEntityList() =
        UserListEntity(
            listOf(
                givenUserEntity("email1"),
                givenUserEntity("email2"),
                givenUserEntity("email3"),
                givenUserEntity("email4"),
            )
        )

    private fun givenUserEntity(email: String): UserEntity {
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

        return UserEntity(email, gender, nameEntity, locationEntity, registeredEntity, phone,
            pictureEntity)
    }

    private fun givenUserList(userEntityList: List<UserEntity>) =
        userEntityList.map { mapUserEntityToDao(it) }

    private fun mapUserEntityToDao(userEntity: UserEntity) =
        User(userEntity.email, userEntity.gender,
            userEntity.name.first, userEntity.name.last,
            userEntity.location.street.name, userEntity.location.city, userEntity.location.state,
            userEntity.registered.date, userEntity.phone,
            userEntity.picture.large, userEntity.picture.medium)
}