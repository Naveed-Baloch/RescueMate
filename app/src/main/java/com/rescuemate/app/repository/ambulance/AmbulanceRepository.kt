package com.rescuemate.app.repository.ambulance

import com.rescuemate.app.dto.Ambulance
import com.rescuemate.app.dto.AmbulanceRequest
import com.rescuemate.app.dto.AmbulanceRequestStatus
import com.rescuemate.app.repository.Result
import kotlinx.coroutines.flow.Flow

interface AmbulanceRepository {
    fun getFirstAvailableAmbulance(city: String): Flow<Result<Ambulance>>
    fun getUserAmbulance(userId: String): Flow<Result<Ambulance>>
    fun addAmbulance(ambulance: Ambulance): Flow<Result<String>>
    fun addAmbulanceRequest(ambulanceRequest: AmbulanceRequest): Flow<Result<String>>
    fun updateRequestStatus(ambulanceRequestStatus: AmbulanceRequestStatus, requestId : String): Flow<Result<String>>
    fun getAmbulanceOwnerRequest(ownerId: String): Flow<Result<List<AmbulanceRequest>>>
}