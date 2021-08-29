package com.random.user.domain

import android.content.Context
import androidx.lifecycle.LiveData
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
    val pictureThumbnail: String)

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(item: List<User>)

    @get:Query("select * from User")
    val userLiveData: LiveData<List<User>?>

    @Query("SELECT * FROM User " +
            "WHERE name LIKE :name || '%' " +
            "OR surname LIKE :surname || '%' " +
            "OR email LIKE :email || '%'")
    fun userFilterLiveData(name: String,
                           surname: String,
                           email: String): LiveData<List<User>?>
}