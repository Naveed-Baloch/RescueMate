package com.rescuemate.app.messaging

data class NotificationReq(
    val message: Message
)

data class Message(
    val token: String,
    val notification: Notification,
    val data: Map<String,Any>
)

data class Notification(
    val title: String,
    val body: String,
)