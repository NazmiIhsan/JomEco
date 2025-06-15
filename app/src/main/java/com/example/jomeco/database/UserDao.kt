package com.example.jomeco.database

import androidx.room.*
import java.security.MessageDigest

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)


    @Query("SELECT * FROM user WHERE email = :email AND password = :hashedPassword LIMIT 1")
    suspend fun getUserByEmailAndPassword(email: String, hashedPassword: String): User?
}
