package com.rescuemate.app

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.rescuemate.app.repository.fcm.FcmRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MyFirebaseMessagingService : FirebaseMessagingService() {
    @Inject
    lateinit var fcmRepository: FcmRepository
    private val notificationID = 123

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        fcmRepository.updateFcmToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        if (message.notification != null) {
            val title = message.notification?.title
            val body = message.notification?.body
            showNotification(title, body)
        }
    }

    private fun showNotification(title: String?, body: String?) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "RescueMateChannelId"
        val channelName = "RescueMateChannelName"
        val notificationSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.drawable.ic_rescue)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setSound(notificationSoundUri)
            .setVibrate(LongArray(1) { 500 }) // Optional vibration
            .setAutoCancel(true)

        // You can customize the notification further based on your requirements
        val channel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)
        notificationManager.notify(notificationID, notificationBuilder.build())
    }
}