package com.rescuemate.app.messaging

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
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
    fun showNotification(data: Map<String, String>) {
        val title = data["title"]
        val body = data["body"]
        val requestId = data["requestId"]

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "RescueMateChannelId"
        val channelName = "RescueMateChannelName"
        val notificationSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val activityIntent = Intent(context, MainActivity::class.java).apply {
            this.data = ("https://rescuemate/$requestId").toUri()
        }
        val pendingIntent = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(activityIntent)
            getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE)
        }

        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setContentTitle(title)
            .setContentText(body)
            .setColor(Color.Black.toArgb())
            .setSmallIcon(R.drawable.ic_ambulance)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setSound(notificationSoundUri)
            .setContentIntent(pendingIntent)

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