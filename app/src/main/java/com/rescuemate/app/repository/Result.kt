package com.rescuemate.app.repository

import java.lang.Exception

sealed class Result<out T> {
    data class Success<out R>(val data:R) : Result<R>()
    data class Failure(val exception: Exception) : Result<Nothing>()
    data object Loading : Result<Nothing>()
}