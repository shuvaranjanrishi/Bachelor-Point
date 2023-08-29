package com.therishideveloper.bachelorpoint.ui.member

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.therishideveloper.bachelorpoint.api.ApiService
import com.therishideveloper.bachelorpoint.api.NetworkResult
import com.therishideveloper.bachelorpoint.model.User
import org.json.JSONObject
import javax.inject.Inject

/**
 * Created by Shuva Ranjan Rishi on 27,August,2023
 * BABL, Bangladesh,
 */

class MemberRepo @Inject constructor(private val apiService: ApiService) {

    private val TAG = "MemberRepo"

    private val _memberLiveData = MutableLiveData<NetworkResult<List<User>>>()
    val memberLiveData : LiveData<NetworkResult<List<User>>>
        get() = _memberLiveData

    suspend fun getMembers(accountId: String) {
        Log.d(TAG, "accountId $accountId")
        _memberLiveData.postValue(NetworkResult.Loading())
        val response = apiService.getMembers(accountId)

        if(response.isSuccessful && response.body()!=null){
            try {
                val jsonObject = JSONObject(response.body()!!.asJsonObject.toString())
                val memberList: MutableList<User> = mutableListOf()
                val keys: Iterator<Any> = jsonObject.keys()
                while (keys.hasNext()) {
                    val key = keys.next().toString()
                    val childObj: JSONObject = jsonObject.getJSONObject(key)
                    memberList.add(
                        User(
                            "" + childObj.getString("id"),
                            "" + childObj.getString("uid"),
                            "" + childObj.getString("accountId"),
                            ""+childObj.getString("name"),
                            ""+childObj.getString("phone"),
                            "" + childObj.getString("address"),
                            "" + childObj.getString("email"),
                            "" + childObj.getString("password"),
                            "" + childObj.getString("usertype"),
                            "" + childObj.getString("online"),
                            "" + childObj.getString("createdAt"),
                            "" + childObj.getString("updatedAt"),
                        )
                    )
                }
                _memberLiveData.postValue(NetworkResult.Success(memberList))

                Log.d(TAG, "MemberList $memberList")
            } catch (e: Exception) {
                Log.e(TAG, "Exception: $e")
            }
        }else if(response.errorBody()!=null){
            _memberLiveData.postValue(NetworkResult.Error("Something went wrong"))
        }else{
            _memberLiveData.postValue(NetworkResult.Error("Something went wrong"))
        }
    }
}