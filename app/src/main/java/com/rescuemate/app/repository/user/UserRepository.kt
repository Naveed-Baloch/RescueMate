package com.rescuemate.app.repository.user

import android.net.Uri
import com.rescuemate.app.dto.User
import kotlinx.coroutines.flow.Flow
import com.rescuemate.app.repository.Result

interface UserRepository {
    fun storeUserToDatabase(user: User): Flow<Result<String>>
    fun uploadUserProfilePic(uri: Uri): Flow<Result<String>>
}