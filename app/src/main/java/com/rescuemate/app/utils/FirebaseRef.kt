package com.rescuemate.app.utils

object FirebaseRef {
    const val PROJECT_ID = "rescuemate-c1591"
    const val USERS = "Users"
    const val LABORATORIES = "Laboratories"
    const val AMBULANCES = "Ambulances"
    const val BLOOD_DONORS = "Blood Donors"
    const val USERS_IMAGES = "Users Images"
    const val AMBULANCE_IMAGES = "Ambulance Images"
    const val LABORATORY_IMAGES = "Laboratory Images"
    const val TOKEN = "token"
    const val AMBULANCE_REQUESTS = "Ambulance Request"
    const val NOTIFICATIONS = "Notifications"
}

//fun getFirebaseRefFromUserType(userType: UserType) = when (userType) {
//    UserType.Patient -> FirebaseRef.PATIENTS
//    UserType.AmbulanceOwner -> FirebaseRef.AMBULANCES
//    UserType.Donor -> FirebaseRef.BLOOD_DONORS
//    UserType.LaboratoryOwner -> FirebaseRef.LABORATORIES
//}