package com.random.user

import android.content.Context
import com.random.user.domain.UserDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class MockUserDataStore(context: Context) : UserDataStore(context) {

    private val deletedUsers = mutableSetOf<String>()

    override val deletedUsersFlow: Flow<Set<String>>
        get() = flowOf(deletedUsers)

    override suspend fun updateDeletedUsers(email: String) {
        deletedUsers.add(email)
    }
}