package com.example.profile.ui.theme

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.jomeco.database.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import kotlinx.coroutines.withContext
import java.io.File
import java.security.MessageDigest

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    var name by mutableStateOf("Loading...")

    var profileImageUri by mutableStateOf<Uri?>(null)

    var showSuccessMessage by mutableStateOf(false)

    var email by mutableStateOf("Loading...")
    var pnumber by mutableStateOf("Loading...")
    var password by mutableStateOf("Loading...")

    var userId: Int = -1


    private val userDao = AppDatabase.getDatabase(application).userDao()

    init {
        loadUserName()
        loadProfileImage()

    }

    private fun loadUserName() {
        val prefs = getApplication<Application>()
            .getSharedPreferences("jomeco_prefs", Context.MODE_PRIVATE)
        val userId = prefs.getInt("current_user_id", -1)

        if (userId != -1) {
            viewModelScope.launch(Dispatchers.IO) {
                val user = userDao.getUserByIds(userId)
                user?.let {
                    name = it.username
                    email = it.email
                    pnumber = it.phoneNumber
                    password = it.password
                } ?: run {
                    name = "Guest"
                    email = "-"
                    pnumber = "-"
                    password = "-"
                }
            }
        } else {
            name = "Guest"
            email = "-"
            pnumber = "-"
            password = "-"
        }
    }


    private fun loadProfileImage() {
        val prefs = getApplication<Application>()
            .getSharedPreferences("jomeco_prefs", Context.MODE_PRIVATE)
        val userId = prefs.getInt("current_user_id", -1)

        if (userId != -1) {
            viewModelScope.launch(Dispatchers.IO) {
                val user = userDao.getUserByIds(userId)
                user?.profilePictureUri?.let { path ->
                    val file = File(path)
                    if (file.exists()) {
                        profileImageUri = Uri.fromFile(file)
                    }
                }
            }
        }
    }







    fun updateProfileImage(uri: Uri) {
        val context = getApplication<Application>().applicationContext
        val prefs = context.getSharedPreferences("jomeco_prefs", Context.MODE_PRIVATE)
        val userId = prefs.getInt("current_user_id", -1)

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val inputStream = context.contentResolver.openInputStream(uri)
                val fileName = "profile_${userId}_${System.currentTimeMillis()}.jpg"
                val file = File(context.filesDir, fileName)

                inputStream?.use { input ->
                    file.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }

                // Save path to DB
                if (userId != -1) {
                    userDao.updateProfileImage(userId, file.absolutePath)
                }

                // Update state on main thread
                withContext(Dispatchers.Main) {
                    profileImageUri = Uri.fromFile(file)
                    showSuccessMessage = true
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun loadUserProfile() {
        val prefs = getApplication<Application>()
            .getSharedPreferences("jomeco_prefs", Context.MODE_PRIVATE)
        val userId = prefs.getInt("current_user_id", -1)

        // Don't launch if userId is invalid
        if (userId != -1) {
            viewModelScope.launch {
                val user = userDao.getUserByIdOnce(userId)
                user?.let {
                    name = it.username
                    profileImageUri = if (it.profilePictureUri.isNullOrEmpty()) null else Uri.fromFile(File(it.profilePictureUri))
                }
            }
        }
    }


    fun encryptPassword(password: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    fun updatePassword(userId: Int, newPassword: String) {
        val hashedPassword = encryptPassword(newPassword)

        viewModelScope.launch(Dispatchers.IO) {
            userDao.updatePassword(userId, hashedPassword)
        }
    }

    fun loadUserData() {
        val context = getApplication<Application>().applicationContext
        val prefs = context.getSharedPreferences("jomeco_prefs", Context.MODE_PRIVATE)
        val loadedUserId = prefs.getInt("current_user_id", -1)

        if (loadedUserId != -1) {
            userId = loadedUserId
            viewModelScope.launch {
                val user = userDao.getUserByIds(userId)
                user?.let {
                    name = it.username
                    email = it.email
                    pnumber = it.phoneNumber
                    password = it.password
                }
            }
        }
    }


    fun updateUserProfile(
        newName: String,
        newEmail: String,
        newPhone: String,
        newPassword: String?, // nullable: only update if not null
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val user = userDao.getUserByIds(userId)
                if (user != null) {
                    val updatedUser = user.copy(
                        username = newName,
                        email = newEmail,
                        phoneNumber = newPhone,
                        password = newPassword?.let { encryptPassword(it) } ?: user.password
                    )
                    userDao.updateUser(updatedUser)

                    // Reflect changes in state
                    name = updatedUser.username
                    email = updatedUser.email
                    pnumber = updatedUser.phoneNumber
                    password = updatedUser.password

                    onSuccess()
                } else {
                    onError("User not found")
                }
            } catch (e: Exception) {
                onError("Failed to update: ${e.message}")
            }
        }
    }

}
