package com.rescuemate.app.dto

data class AmbulanceRequest(
    val patient: User = User(),
    val ambulance: Ambulance = Ambulance(),
    val address: String = "",
    val lat: Double = 0.0,
    val lag: Double = 0.0,
    val status: AmbulanceRequestStatus = AmbulanceRequestStatus.Pending,
)

enum class AmbulanceRequestStatus {
    Pending, Accepted, Completed, Rejected
}
