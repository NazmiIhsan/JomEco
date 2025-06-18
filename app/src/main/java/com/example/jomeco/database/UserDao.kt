package com.example.jomeco.database

import androidx.room.*
import java.security.MessageDigest

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)


    @Query("SELECT * FROM user WHERE email = :email AND password = :hashedPassword LIMIT 1")
    suspend fun getUserByEmailAndPassword(email: String, hashedPassword: String): User?

    @Query("SELECT * FROM user WHERE id = :id")
    fun getUserById(id: Int): kotlinx.coroutines.flow.Flow<User>
    @Update
    suspend fun updateUser(user: User)



    @Query("SELECT * FROM user WHERE id = :id")
    suspend fun getUserByIdOnce(id: Int): User


    @Query("SELECT * FROM user WHERE id = :userId LIMIT 1")
    suspend fun getUserByIds(userId: Int): User?



    @Query("UPDATE user SET profilePictureUri = :imageUri WHERE id = :userId")
    suspend fun updateProfileImage(userId: Int, imageUri: String)


    @Query("SELECT * FROM user LIMIT 1")
    suspend fun getCurrentUser(): User

    @Query("UPDATE user SET password = :newPassword WHERE id = :userId")
    suspend fun updatePassword(userId: Int, newPassword: String)











}
