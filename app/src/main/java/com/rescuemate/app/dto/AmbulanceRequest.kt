package com.rescuemate.app.dto

data class AmbulanceRequest(
    val id: String = "",
    val patient: User = User(),
    val ambulance: Ambulance = Ambulance(),
    val address: String = "",
    val lat: Double = 0.0,
    val lag: Double = 0.0,
    val status: AmbulanceRequestStatus = AmbulanceRequestStatus.Pending,
) {
    companion object
}

enum class AmbulanceRequestStatus {
    Pending, Accepted, Completed, Rejected
}


val AmbulanceRequest.Companion.mock by lazy {
    AmbulanceRequest(
        id = "gubergren", patient = User(
            userId = "ullamcorper",
            name = "James Fry",
            email = "dino.roman@example.com",
            profileUri = "quam",
            profilePicUrl = "https://www.google.com/#q=dolores",
            cnic = "suspendisse",
            userType = UserType.Donor,
            password = "sea",
            token = "recteque",
            phoneNumber = "(551) 606-6284"
        ), ambulance = Ambulance(ownerId = "lacinia", vehicleNumber = "petentium", licenceNumber = "referrentur", city = "Mos Eisley", isAvailable = false), address = "quot", lat = 12.13, lag = 14.15, status = AmbulanceRequestStatus.Pending
    )
}