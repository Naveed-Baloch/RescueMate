package com.rescuemate.app.repository.user

import com.rescuemate.app.dto.User
import kotlinx.coroutines.flow.Flow
import com.rescuemate.app.repository.Result

interface UserRepository {
    fun storeUserToDatabase(user: User): Flow<Result<String>>
}