package com.therishideveloper.bachelorpoint.ui.member

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
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

    private val _membersLiveData = MutableLiveData<NetworkResult<List<User>>>()
    val membersLiveData get() = _membersLiveData

    private val _memberLiveData = MutableLiveData<NetworkResult<User>>()
    val memberLiveData  get() = _memberLiveData

    suspend fun getMembers(accountId: String): List<User> {
        _membersLiveData.postValue(NetworkResult.Loading())
        val response = apiService.getMembers(accountId)
        val memberList: MutableList<User> = mutableListOf()

        if (response.isSuccessful && response.body() != null) {
            try {
                val jsonObject = JSONObject(response.body()!!.asJsonObject.toString())
                val keys: Iterator<Any> = jsonObject.keys()
                while (keys.hasNext()) {
                    val key = keys.next().toString()
                    val childObj: JSONObject = jsonObject.getJSONObject(key)

                    val user = Gson().fromJson(
                        childObj.toString(), User::class.java
                    ) as User
                    memberList.add(user)
                }
                _membersLiveData.postValue(NetworkResult.Success(memberList.sortedBy { it.name }))

                Log.d(TAG, "MemberList $memberList")
            } catch (e: Exception) {
                Log.e(TAG, "Exception: $e")
            }
        }else if(response.errorBody()!=null){
            val jsonObject = JSONObject(response.errorBody()!!.charStream().readText())
            _membersLiveData.postValue(NetworkResult.Error(jsonObject.getString("message")))
        }else{
            _membersLiveData.postValue(NetworkResult.Error("Something went wrong"))
        }
        return memberList
    }

    suspend fun getMember(uid:String){
        _memberLiveData.postValue(NetworkResult.Loading())
        val response = apiService.getMember(uid)
        if (response.isSuccessful && response.body() != null) {
            val user: User = response.body()!!
            _memberLiveData.postValue(NetworkResult.Success(user))
        } else if (response.errorBody() != null) {
            val jsonObject = JSONObject(response.errorBody()!!.charStream().readText())
            _memberLiveData.postValue(NetworkResult.Error(jsonObject.getString("message")))
        } else {
            _memberLiveData.postValue(NetworkResult.Error("Something went wrong"))
        }
    }
}