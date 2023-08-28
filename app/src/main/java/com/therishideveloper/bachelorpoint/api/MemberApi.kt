package com.therishideveloper.bachelorpoint.api

import com.google.gson.JsonObject
import com.therishideveloper.bachelorpoint.model.User
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Shuva Ranjan Rishi on 28,August,2023
 * BABL, Bangladesh,
 */

interface MemberApi {

    @GET("BachelorPoint/Accounts/U8Xt0HLykUMYo8Kkwz29envO6K02/Members.json?auth=t9KWKmLIul65TR5thTML9Wz8HGDs43MW1Kc3byRr")
    suspend fun getMembers(): JsonObject

//    @GET("BachelorPoint/Users/MNiH8Zh5ESULp0AHFf4VZmH3IG72/Members?auth=t9KWKmLIul65TR5thTML9Wz8HGDs43MW1Kc3byRr")
//    suspend fun getMembers(): Response<JsonObject>
//    @GET("BachelorPoint/Users/{accountId}/Members")
//    suspend fun getMembers(@Query("accountId") accountId:String): Response<ResponseBody>

    @GET("api/offers")
    fun loadOffers(): Call<String?>?


//        database.child(accountId).child("Members")
//            .addListenerForSingleValueEvent(
//                object : ValueEventListener {
//                    override fun onDataChange(dataSnapshot: DataSnapshot) {
//                        val memberList: MutableList<User> = mutableListOf()
//                        for (ds in dataSnapshot.children) {
//                            val user: User? = ds.getValue(User::class.java)
//                            memberList.add(user!!)
//                        }
//
//                        Log.e(TAG,"List: "+memberList.sortedBy { it.name })
//                    }
//
//                    override fun onCancelled(error: DatabaseError) {
//                        Log.e(TAG, "DatabaseError", error.toException())
//                    }
//                }
//            )

}