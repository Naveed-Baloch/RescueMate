package com.rescuemate.app.messaging

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.rescuemate.app.repository.fcm.FcmRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MyFirebaseMessagingService : FirebaseMessagingService() {
    @Inject
    lateinit var fcmRepository: FcmRepository

    @Inject
    lateinit var pushNotificationManager: PushNotificationManager

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        fcmRepository.updateFcmToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        if (message.notification != null) {
            val title = message.notification?.title
            val body = message.notification?.body
            pushNotificationManager.showNotification(title, body, message.data.toMap())
        }
    }

}