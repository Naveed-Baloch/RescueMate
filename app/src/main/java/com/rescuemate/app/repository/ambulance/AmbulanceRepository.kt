package com.rescuemate.app.repository.ambulance

import com.rescuemate.app.dto.Ambulance
import com.rescuemate.app.dto.AmbulanceRequest
import com.rescuemate.app.dto.AmbulanceRequestStatus
import com.rescuemate.app.dto.UserType
import com.rescuemate.app.repository.Result
import kotlinx.coroutines.flow.Flow

interface AmbulanceRepository {
    fun getFirstAvailableAmbulance(city: String): Flow<Result<Ambulance>>
    fun getAmbulanceRequest(requestId: String): Flow<Result<AmbulanceRequest>>
    fun getUserAmbulance(userId: String): Flow<Result<Ambulance>>
    fun addAmbulance(ambulance: Ambulance): Flow<Result<String>>
    fun addAmbulanceRequest(ambulanceRequest: AmbulanceRequest): Flow<Result<String>>
    fun updateRequestStatus(ambulanceRequestStatus: AmbulanceRequestStatus, requestId : String): Flow<Result<String>>
    fun getAmbulanceOwnerRequest(ownerId: String, userType: UserType): Flow<Result<List<AmbulanceRequest>>>
}