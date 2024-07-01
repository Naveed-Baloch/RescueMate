package com.rescuemate.app.repository.auth

import android.app.Activity
import android.net.Uri
import com.rescuemate.app.dto.User
import kotlinx.coroutines.flow.Flow
import com.rescuemate.app.repository.Result

interface AuthRepository {
    fun storeUserToDatabase(user: User): Flow<Result<String>>
    fun uploadUserProfilePic(uri: Uri, userId: String): Flow<Result<String>>
    fun signUp(user: User): Flow<Result<String>>
    fun login(email: String, password: String): Flow<Result<String>>
    fun fetchUserDetails(userId: String, dbNodeRef: String): Flow<Result<User>>
    fun signOutFirebaseUser()
}