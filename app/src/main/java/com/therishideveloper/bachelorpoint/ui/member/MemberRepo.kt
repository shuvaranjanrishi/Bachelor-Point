package com.therishideveloper.bachelorpoint.ui.member

import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.therishideveloper.bachelorpoint.api.MemberApi
import com.therishideveloper.bachelorpoint.model.User
import org.json.JSONException
import org.json.JSONObject
import javax.inject.Inject

/**
 * Created by Shuva Ranjan Rishi on 27,August,2023
 * BABL, Bangladesh,
 */

class MemberRepo @Inject constructor(private val memberApi: MemberApi) {

    private val TAG = "MemberRepo"

    suspend fun getMembers(database: DatabaseReference, accountId: String) {
        Log.d("TAG", "accountId $accountId")
        val response = memberApi.getMembers()
        Log.d("TAG", "response ${response["Rqhh3yBTXxTnHguHMleuh2v9sFj1"]}")

        val jsonObject = JSONObject(response.asJsonObject.toString())
        Log.d("TAG", "jsonObject ${jsonObject}")
        val memberList: MutableList<User> = mutableListOf()
        val keys: Iterator<Any> = jsonObject.keys()
        while (keys.hasNext()) {
            val key = keys.next().toString() // this will be your JsonObject key
            val childObj: JSONObject = jsonObject.getJSONObject(key)
            Log.d(TAG,"obj: "+childObj)
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
            Log.d(TAG,"lista; "+memberList)
        }
    }
}