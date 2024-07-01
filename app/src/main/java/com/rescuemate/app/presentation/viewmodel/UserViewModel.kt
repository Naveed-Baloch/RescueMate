package com.rescuemate.app.presentation.viewmodel

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rescuemate.app.dto.User
import com.rescuemate.app.extensions.isVisible
import com.rescuemate.app.extensions.showToast
import com.rescuemate.app.repository.Result
import com.rescuemate.app.repository.auth.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    var isLoading by mutableStateOf(false)

    fun signOutUser() = authRepository.signOutFirebaseUser()

    fun login(email: String, password: String) = authRepository
        .login(email, password).onEach {
            isLoading = it is Result.Loading
        }

    fun signup(user: User) = authRepository.signUp(user).onEach {
        isLoading = it is Result.Loading
    }

    fun fetchUserDetails(userId: String, dbNodeRef: String) = authRepository.fetchUserDetails(userId, dbNodeRef).onEach {
        isLoading = it is Result.Loading
    }

    fun storeUserToDb(user: User) = authRepository
        .storeUserToDatabase(user)
        .onEach {
            isLoading = it is Result.Loading
        }

    fun uploadUserProfile(uri: Uri, userId: String) = authRepository
        .uploadUserProfilePic(uri, userId)
        .onEach {
            isLoading = it is Result.Loading
        }

}