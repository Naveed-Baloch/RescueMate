package com.rescuemate.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rescuemate.app.repository.fcm.FcmRepository
import com.rescuemate.app.repository.fcm.NotificationReq
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FcmVM @Inject constructor(
    private val fcmRepository: FcmRepository,
) : ViewModel() {

    fun refreshToken() = fcmRepository.refreshFcmToken()

    suspend fun sendPushNotification(notificationReq: NotificationReq) = fcmRepository.sendPushNotification(notificationReq)

}