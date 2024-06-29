package com.rescuemate.app.repository.auth

import android.app.Activity
import com.rescuemate.app.dto.User
import kotlinx.coroutines.flow.Flow
import com.rescuemate.app.repository.Result

interface AuthRepository {
//    fun sendOtp(phone: String, activity: Activity?): Flow<Result<String>>
    fun signUp(otp: String, user: User): Flow<Result<String>>
    fun login(email: String, password: String): Flow<Result<String>>
    fun signOutFirebaseUser()
}