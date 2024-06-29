package com.rescuemate.app.network

import com.rescuemate.app.repository.fcm.NotificationReq
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @POST("{projectId}/messages:send")
    suspend fun sendPushNotification(
        @Path("projectId") firebaseProjectId: String,
        @Header("Authorization") authHeader: String,
        @Body notificationReq: NotificationReq
    )
}