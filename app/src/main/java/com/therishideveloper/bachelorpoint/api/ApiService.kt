package com.therishideveloper.bachelorpoint.api

import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by Shuva Ranjan Rishi on 28,August,2023
 * BABL, Bangladesh,
 */

interface ApiService {

    @GET("Accounts/{accountId}/Members.json")
    suspend fun getMembers(@Path("accountId") accountId: String): Response<JsonObject>


    @GET("Bachelor Point/Accounts/{accountId}/Members.json")
    suspend fun signIn(@Path("accountId") accountId: String): Response<JsonObject>
}