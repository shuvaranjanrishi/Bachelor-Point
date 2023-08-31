package com.therishideveloper.bachelorpoint.api

import android.database.Observable
import android.provider.ContactsContract.Profile
import com.google.gson.JsonObject
import com.therishideveloper.bachelorpoint.model.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


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
}