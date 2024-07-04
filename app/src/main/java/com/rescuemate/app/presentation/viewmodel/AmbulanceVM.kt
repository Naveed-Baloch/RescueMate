package com.rescuemate.app.presentation.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rescuemate.app.dto.Ambulance
import com.rescuemate.app.dto.AmbulanceRequest
import com.rescuemate.app.dto.AmbulanceRequestStatus
import com.rescuemate.app.dto.User
import com.rescuemate.app.dto.UserType
import com.rescuemate.app.extensions.showToast
import com.rescuemate.app.repository.Result
import com.rescuemate.app.repository.ambulance.AmbulanceRepository
import com.rescuemate.app.repository.auth.AuthRepository
import com.rescuemate.app.sharedpref.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AmbulanceVM @Inject constructor(
    private val userPreferences: UserPreferences,
    private val ambulanceRepository: AmbulanceRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {
    var isLoading by mutableStateOf(false)
    var userAmbulance by mutableStateOf<Ambulance?>(null)
    val user by lazy { userPreferences.getUser() }

    fun addAmbulance(ambulance: Ambulance) = ambulanceRepository.addAmbulance(ambulance).onEach {
        isLoading = it is Result.Loading
    }

    fun getUserAmbulance() = ambulanceRepository.getUserAmbulance(user?.userId ?: "").onEach {
        isLoading = it is Result.Loading
        if (it is Result.Success) {
            userAmbulance = it.data
        }
    }

    private fun addRequest(ambulanceRequest: AmbulanceRequest) = ambulanceRepository.addAmbulanceRequest(ambulanceRequest)


    fun processAddRequest(city: String, lat: Double, lng: Double, address: String, onSuccess: () -> Unit, context: Context) {
        isLoading = true
        viewModelScope.launch {
            ambulanceRepository
                .getFirstAvailableAmbulance(city)
                .collect { ambulanceRes ->
                    when (ambulanceRes) {
                        is Result.Success -> {
                            val ambulance = ambulanceRes.data
                            val ambulanceRequest = AmbulanceRequest(patient = user!!, ambulance = ambulance, address = address, lat = lat, lag = lng, status = AmbulanceRequestStatus.Pending)

                            addRequest(ambulanceRequest).collect { addRequestResult ->
                                when (addRequestResult) {
                                    is Result.Failure -> {
                                        isLoading = false
                                        context.showToast(addRequestResult.exception.message ?: "Something went wrong")
                                    }

                                    is Result.Success -> {
                                        authRepository.fetchUserDetails(ambulance.ownerId).collect { ambulanceOwnerRes ->
                                            when (ambulanceOwnerRes) {
                                                is Result.Failure -> {
                                                    isLoading = false
                                                    context.showToast(ambulanceOwnerRes.exception.message ?: "Something went wrong")
                                                }

                                                is Result.Success -> {
                                                    isLoading = false
                                                    val ambulanceOwner = ambulanceOwnerRes.data
                                                    context.showToast("Ambulance is added")
                                                    onSuccess()
                                                    // TODO: Send Notification to Ambulance Owner using the FCM Token
                                                }

                                                else -> {}
                                            }
                                        }
                                    }

                                    else -> {}
                                }
                            }
                        }

                        is Result.Failure -> {
                            isLoading = false
                            context.showToast(ambulanceRes.exception.message ?: "Something went wrong")
                        }

                        else -> {}
                    }
                }
        }

    }

}