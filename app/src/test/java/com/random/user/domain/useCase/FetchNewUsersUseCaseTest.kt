package com.random.user.domain.useCase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.*
import com.random.user.CoroutineRule
import com.random.user.data.User
import com.random.user.data.UserDao
import com.random.user.data.UserDataStore
import com.random.user.data.UserRepository
import com.random.user.data.mapper.UserEntityToDaoMapper
import com.random.user.data.model.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

@ExperimentalCoroutinesApi
class FetchNewUsersUseCaseTest {

    private val mockUserRepository: UserRepository = mock()

    private val mockUserDataStore: UserDataStore = mock()

    private val mockUserDao: UserDao = mock()

    private val mockUserEntityToDaoMapper: UserEntityToDaoMapper = mock()

    private lateinit var fetchNewUsersUseCase: FetchNewUsersUseCase

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineRule = CoroutineRule()

    @Before
    fun onSetup() {
        fetchNewUsersUseCase = FetchNewUsersUseCase(
            mockUserRepository,
            mockUserDataStore,
            mockUserDao,
            mockUserEntityToDaoMapper
        )
    }

    @Test
    fun `when fetch new users use case is executed then users are fetched, filtered and inserted in the database`()
            = coroutineRule.runBlockingTest {
        // given
        val numberOfUsers = 4
        val userEntityList = givenUserEntityList()
        given(mockUserRepository.fetchNewUsers(numberOfUsers)).willReturn(userEntityList)

        val userDaoList = givenUserDaoList(userEntityList.results)
        for ((index, user) in userEntityList.results.withIndex()) {
            given(mockUserEntityToDaoMapper.userEntityToDao(user))
                .willReturn(userDaoList[index])
        }

        val deletedUsers = setOf("email2", "email3")
        val deletedUsersFlow = flow {
            emit(deletedUsers)
        }
        whenever(mockUserDataStore.deletedUsersFlow).thenReturn(deletedUsersFlow)

        val filteredList = listOf(userDaoList[0], userDaoList[3])

        // when
        fetchNewUsersUseCase.execute(numberOfUsers)

        // then
        verify(mockUserRepository).fetchNewUsers(numberOfUsers)
        verify(mockUserDao).insertUsers(filteredList)
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

    private fun givenUserDaoList(userEntityList: List<UserEntity>) =
        userEntityList.map { mapUserEntityToDao(it) }

    private fun mapUserEntityToDao(userEntity: UserEntity) =
        User(userEntity.email, userEntity.gender,
            userEntity.name.first, userEntity.name.last,
            userEntity.location.street.name, userEntity.location.city, userEntity.location.state,
            userEntity.registered.date, userEntity.phone,
            userEntity.picture.large, userEntity.picture.medium)
}