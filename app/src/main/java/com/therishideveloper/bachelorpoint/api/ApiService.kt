package com.therishideveloper.bachelorpoint.api

import com.google.gson.JsonObject
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by Shuva Ranjan Rishi on 28,August,2023
 * BABL, Bangladesh,
 */

interface ApiService {

    @GET("BachelorPoint/Accounts/{accountId}/Members.json")
    suspend fun getMembers(@Path("accountId") accountId: String): JsonObject

}