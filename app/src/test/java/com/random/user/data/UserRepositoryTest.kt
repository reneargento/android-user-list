package com.random.user.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.random.user.CoroutineRule
import com.random.user.data.mapper.UserDaoToUserDomainMapper
import com.random.user.data.mapper.UserEntityToDaoMapper
import com.random.user.data.model.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

@ExperimentalCoroutinesApi
class UserRepositoryTest {

    private val mockUserNetwork: UserNetwork = mock()

    private val mockUserDao: UserDao = mock()

    private val mockUserDataStore: UserDataStore = mock()

    private val mockUserEntityToDaoMapper: UserEntityToDaoMapper = mock()

    private val mockUserDaoToUserDomainMapper: UserDaoToUserDomainMapper = mock()

    private lateinit var userRepository: UserRepositoryImpl

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineRule = CoroutineRule()

    @Before
    fun onSetup() {
        userRepository = UserRepositoryImpl(
            mockUserNetwork,
            mockUserDao,
            mockUserDataStore,
            mockUserEntityToDaoMapper,
            mockUserDaoToUserDomainMapper
        )
    }

    @Test
    fun `when users are fetched then users are filtered and inserted in the database`()
    = coroutineRule.runBlockingTest {
        // given
        val numberOfUsers = 4
        val userEntityList = givenUserEntityList()
        given(mockUserNetwork.fetchUsers(numberOfUsers)).willReturn(userEntityList)
        val userDaoList = givenUserDaoList(userEntityList.results)

        val deletedUsers = setOf("email2", "email3")
        val deletedUsersFlow = flow { emit(deletedUsers) }
        whenever(mockUserDataStore.deletedUsersFlow).thenReturn(deletedUsersFlow)

        val filteredList = listOf(userDaoList[0], userDaoList[3])

        // when
        userRepository.fetchNewUsers(numberOfUsers)

        // then
        verify(mockUserNetwork).fetchUsers(numberOfUsers)
        verify(mockUserDao).insertUsers(filteredList)
    }

    @Test
    fun `when local users are queried then the database is queried`() = coroutineRule.runBlockingTest {
        // when
        userRepository.queryLocalUsers()

        // then
        verify(mockUserDao).queryAllUsers()
    }

    @Test
    fun `when user is deleted then the database is updated`() = coroutineRule.runBlockingTest {
        // given
        val email = "email"

        // when
        userRepository.deleteUser(email)

        // then
        verify(mockUserDao).deleteUser(email)
        verify(mockUserDataStore).updateDeletedUsers(email)
    }

    @Test
    fun `when users are filtered then the database is queried`() = coroutineRule.runBlockingTest {
        // given
        val filter = "filter"

        // when
        userRepository.queryUsersWithFilter(filter)

        // then
        verify(mockUserDao).queryUsersWithFilter(filter)
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

    private fun givenUserDaoList(userEntityList: List<UserEntity>) : List<User> {
        val userDaoList = userEntityList.map { mapUserEntityToDao(it) }

        for ((index, user) in userEntityList.withIndex()) {
            given(mockUserEntityToDaoMapper.userEntityToDao(user))
                .willReturn(userDaoList[index])
        }
        return userDaoList
    }

    private fun mapUserEntityToDao(userEntity: UserEntity) =
        User(userEntity.email, userEntity.gender,
            userEntity.name.first, userEntity.name.last,
            userEntity.location.street.name, userEntity.location.city, userEntity.location.state,
            userEntity.registered.date, userEntity.phone,
            userEntity.picture.large, userEntity.picture.medium)
}