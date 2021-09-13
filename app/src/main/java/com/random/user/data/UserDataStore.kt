package com.random.user.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

open class UserDataStore(private val context: Context) {

    companion object {
        private val Context.dataStore by preferencesDataStore(name = "settings")
        private val deletedUsersKey = stringSetPreferencesKey("deletedUsers")
    }

    open val deletedUsersFlow: Flow<Set<String>>
        get() = context.dataStore.data.map { preferences ->
            preferences[deletedUsersKey] ?: setOf()
        }

    open suspend fun updateDeletedUsers(email: String) {
        context.dataStore.edit { preferences ->
            val users = preferences[deletedUsersKey] ?: setOf()
            preferences[deletedUsersKey] = users.plus(email)
        }
    }
}