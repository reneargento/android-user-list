package com.random.user.data

import android.content.Context
import androidx.room.*

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class UserDatabase: RoomDatabase() {
    abstract val userDao: UserDao
}

private lateinit var INSTANCE: UserDatabase

fun getDatabase(context: Context): UserDatabase {
    synchronized(UserDatabase::class) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room
                .databaseBuilder(
                    context.applicationContext,
                    UserDatabase::class.java,
                    "users_db"
                )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
    return INSTANCE
}

@Entity
data class User constructor(
    @PrimaryKey val email: String,
    val gender: String,
    val name: String,
    val surname: String,
    val street: String,
    val city: String,
    val state: String,
    val registered: String,
    val phone: String,
    val pictureLarge: String,
    val pictureMedium: String)

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(item: List<User>)

    @Query("select * from User")
    suspend fun queryAllUsers(): List<User>?

    @Query("SELECT * FROM User " +
            "WHERE name LIKE :filter || '%' " +
            "OR surname LIKE :filter || '%' " +
            "OR email LIKE :filter || '%'")
    fun queryUsersWithFilter(filter: String): List<User>?

    @Query("delete from User where email = :email")
    suspend fun deleteUser(email: String)
}