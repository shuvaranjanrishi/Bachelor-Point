package com.therishideveloper.bachelorpoint.api

import com.google.gson.JsonObject
import com.therishideveloper.bachelorpoint.model.Meal
import com.therishideveloper.bachelorpoint.model.User
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

    @GET("Accounts/{accountId}/Members.json")
    suspend fun signIn(@Path("accountId") accountId: String): Response<JsonObject>

    @GET("Users/{uid}.json")
    suspend fun getMember(@Path("uid") uid: String): Response<User>

    @GET("Accounts/{accountId}/Meal/{monthAndYear}/{date}.json")
    suspend fun getMealListOfADay(
        @Path("accountId") accountId: String,
        @Path("monthAndYear") monthAndYear: String,
        @Path("date") date: String
    ): Response<JsonObject>

    @GET("Accounts/{accountId}/Meal/{monthAndYear}.json")
    suspend fun getMealListOfAMonth(
        @Path("accountId") accountId: String,
        @Path("monthAndYear") monthAndYear: String,
    ): Response<JsonObject>
}