package com.therishideveloper.bachelorpoint.ui.member

import android.util.Log
import com.therishideveloper.bachelorpoint.api.ApiService
import com.therishideveloper.bachelorpoint.model.User
import org.json.JSONObject
import javax.inject.Inject

/**
 * Created by Shuva Ranjan Rishi on 27,August,2023
 * BABL, Bangladesh,
 */

class MemberRepo @Inject constructor(private val memberApi: ApiService) {

    private val TAG = "MemberRepo"

    suspend fun getMembers(accountId: String) {
        Log.d(TAG, "accountId $accountId")
        val response = memberApi.getMembers(accountId)

        val jsonObject = JSONObject(response.asJsonObject.toString())
        val memberList: MutableList<User> = mutableListOf()
        val keys: Iterator<Any> = jsonObject.keys()
        while (keys.hasNext()) {
            val key = keys.next().toString()
            val childObj: JSONObject = jsonObject.getJSONObject(key)
            memberList.add(User(
                ""+childObj.getString("id"),
                ""+childObj.getString("uid"),
                ""+childObj.getString("accountId"),
                ""+childObj.getString("name"),
                ""+childObj.getString("phone"),
                ""+childObj.getString("address"),
                ""+childObj.getString("email"),
                ""+childObj.getString("password"),
                ""+childObj.getString("usertype"),
                ""+childObj.getString("online"),
                ""+childObj.getString("createdAt"),
                ""+childObj.getString("updatedAt"),
            ))
        }
        Log.d(TAG, "MemberList $memberList")
    }
}