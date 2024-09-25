@file:OptIn(ExperimentalSerializationApi::class)

package com.rescuemate.app.messaging

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.ExperimentalSerializationApi

data class NotificationReq(
    val message: Message
)

data class Message(
    val token: String,
    val data: Map<String,Any>,
    @SerializedName("android")
    val priority: Priority
)

data class Priority(
    val priority: String
)