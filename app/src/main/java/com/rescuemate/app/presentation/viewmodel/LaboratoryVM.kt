package com.rescuemate.app.presentation.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rescuemate.app.dto.Laboratory
import com.rescuemate.app.dto.LaboratoryTest
import com.rescuemate.app.repository.Result
import com.rescuemate.app.repository.laboratory.LaboratoryRepository
import com.rescuemate.app.sharedpref.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LaboratoryVM @Inject constructor(private val userPreferences: UserPreferences, private val laboratoryRepository: LaboratoryRepository) : ViewModel() {
    var laboratories by mutableStateOf<List<Laboratory>>(emptyList())
    var isLoading by mutableStateOf(false)
    var userLab by mutableStateOf<Laboratory?>(null)
    val user by lazy { userPreferences.getUser() }

    fun getLaboratories(city: String) = laboratoryRepository
        .getLaboratories(city)
        .onEach {
            isLoading = it is Result.Loading
            when (it) {
                is Result.Success -> {
                    laboratories = it.data
                }

                else -> {}
            }
        }

    fun addLaboratory(laboratory: Laboratory) = laboratoryRepository.addLaboratory(laboratory).onEach {
        isLoading = it is Result.Loading
    }

    fun addLaboratoryTest(laboratoryTest: LaboratoryTest, laboratoryId: String) = laboratoryRepository.addLaboratoryTest(laboratoryTest,laboratoryId).onEach {
        isLoading = it is Result.Loading
    }

    fun getUserLaboratory() = laboratoryRepository.getUserLaboratory(user?.userId ?: "").onEach {
        isLoading = it is Result.Loading
        if(it is Result.Success) {
            userLab = it.data
        }
    }
}