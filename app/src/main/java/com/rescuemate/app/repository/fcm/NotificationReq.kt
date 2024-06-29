package com.rescuemate.app.repository.fcm

data class NotificationReq(
    val message: Message
)

data class Message(
    val token: String,
    val notification: Notification
)

data class Notification(
    val title: String,
    val body: String,
)