package com.rescuemate.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.rescuemate.app.dto.User
import com.rescuemate.app.sharedpref.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserStorageVM @Inject constructor(private val userPreferences: UserPreferences) :
    ViewModel() {
    val user by lazy { userPreferences.getUser() }
    fun setUser(user: User) = userPreferences.updateUser(user)
    fun removeUserData() = userPreferences.removeUserData()

}