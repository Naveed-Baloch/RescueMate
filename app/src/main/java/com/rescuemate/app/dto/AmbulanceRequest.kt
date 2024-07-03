package com.rescuemate.app.dto

data class AmbulanceRequest(
    val patientId: String = "",
    val ambulanceOwnerId: String = "",
    val lat: Double = 0.0,
    val lag: Double = 0.0,
    val status: AmbulanceRequestStatus = AmbulanceRequestStatus.Pending,
)

enum class AmbulanceRequestStatus {
    Pending, Accepted, Completed, Rejected
}
