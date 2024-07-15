package com.rescuemate.app.repository.fcm

import com.rescuemate.app.messaging.NotificationReq

interface FcmRepository {
    suspend fun sendPushNotification(notificationReq: NotificationReq)
    fun refreshFcmToken()
    fun updateFcmToken(token: String)
}