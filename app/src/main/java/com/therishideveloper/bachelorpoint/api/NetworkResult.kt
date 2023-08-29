package com.therishideveloper.bachelorpoint.api

/**
 * Created by Shuva Ranjan Rishi on 29,August,2023
 * BABL, Bangladesh,
 */

sealed class NetworkResult<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : NetworkResult<T>(data)
    class Error<T>(message: String?, data: T? = null) : NetworkResult<T>(data, message)
    class Loading<T> : NetworkResult<T>()
}