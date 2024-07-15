package com.rescuemate.app.messaging

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.gson.Gson
import com.rescuemate.app.MainActivity
import com.rescuemate.app.R
import com.rescuemate.app.dto.AmbulanceRequest
import com.rescuemate.app.extensions.parseString
import dagger.hilt.android.qualifiers.ApplicationContext
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class PushNotificationManager @Inject constructor(
    @ApplicationContext val context: Context,
) {
    private val notificationID = 123
    fun showNotification(title: String?, body: String?, data: Map<String, String>) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "RescueMateChannelId"
        val channelName = "RescueMateChannelName"
        val notificationSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.drawable.ic_rescue)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setSound(notificationSoundUri)
            .apply {
                if(data.isNotEmpty()) {
                    val intent = Intent(context, MainActivity::class.java).apply {
                        this.data  = Uri.parse("https://rescuemate/request/${data["requestId"]}")
                    }
                    val activity = PendingIntent.getActivity(context, notificationID, intent, PendingIntent.FLAG_IMMUTABLE)
                    this.setContentIntent(activity)
                }
            }


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