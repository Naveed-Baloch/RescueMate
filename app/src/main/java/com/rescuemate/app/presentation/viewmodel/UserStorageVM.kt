package com.rescuemate.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.rescuemate.app.dto.User
import com.rescuemate.app.sharedpref.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserStorageVM @Inject constructor(private val userPreferences: UserPreferences) :
    ViewModel() {
    fun setUser(user: User) = userPreferences.updateUser(user)
    fun removeUserData() = userPreferences.removeUserData()

    fun getPayloadRequestId() = userPreferences.payloadRequestId
    fun setPayloadRequestId(value: String?) {
        userPreferences.payloadRequestId = value
    }

    fun getUser() = userPreferences.getUser()

}