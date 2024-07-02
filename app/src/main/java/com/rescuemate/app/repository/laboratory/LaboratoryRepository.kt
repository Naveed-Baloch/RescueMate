package com.rescuemate.app.repository.laboratory

import com.rescuemate.app.dto.BloodDonor
import com.rescuemate.app.dto.Laboratory
import com.rescuemate.app.dto.LaboratoryTest
import com.rescuemate.app.repository.Result
import kotlinx.coroutines.flow.Flow

interface LaboratoryRepository {
    fun getLaboratories(city: String): Flow<Result<List<Laboratory>>>
    fun getUserLaboratory(userId: String): Flow<Result<Laboratory>>
    fun addLaboratory(laboratory: Laboratory): Flow<Result<String>>
    fun addLaboratoryTest(laboratoryTest: LaboratoryTest, laboratoryId: String): Flow<Result<String>>
}