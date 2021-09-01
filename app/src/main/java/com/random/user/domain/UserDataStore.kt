package com.random.user.domain

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserDataStore @Inject constructor(
    @ApplicationContext private val context: Context) {

    companion object {
        private val Context.dataStore by preferencesDataStore(name = "settings")
        private val deletedUsersKey = stringSetPreferencesKey("deletedUsers")
    }

    val deletedUsersFlow: Flow<Set<String>>
        get() = context.dataStore.data.map { preferences ->
            preferences[deletedUsersKey] ?: setOf()
        }

    suspend fun updateDeletedUsers(email: String) {
        context.dataStore.edit { preferences ->
            val users = preferences[deletedUsersKey] ?: setOf()
            preferences[deletedUsersKey] = users.plus(email)
        }
    }
}