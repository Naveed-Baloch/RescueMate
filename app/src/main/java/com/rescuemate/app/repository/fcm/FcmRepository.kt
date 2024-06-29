package com.rescuemate.app.repository.fcm

import com.rescuemate.app.dto.User

interface FcmRepository {
    suspend fun sendPushNotification(notificationReq: NotificationReq)
    fun refreshFcmToken()
    fun updateFcmToken(token: String, user: User)
}