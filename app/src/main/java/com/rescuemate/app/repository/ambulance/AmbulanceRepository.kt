package com.rescuemate.app.repository.ambulance

import com.rescuemate.app.dto.Ambulance
import com.rescuemate.app.dto.Laboratory
import com.rescuemate.app.dto.LaboratoryTest
import com.rescuemate.app.repository.Result
import kotlinx.coroutines.flow.Flow

interface AmbulanceRepository {
    fun getAmbulances(city: String): Flow<Result<List<Ambulance>>>
    fun getUserAmbulance(userId: String): Flow<Result<Ambulance>>
    fun addAmbulance(ambulance: Ambulance): Flow<Result<String>>
}