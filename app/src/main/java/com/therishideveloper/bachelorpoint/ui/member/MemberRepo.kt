package com.therishideveloper.bachelorpoint.ui.member

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
    val membersLiveData : LiveData<NetworkResult<List<User>>>
        get() = _membersLiveData

    private val _memberLiveData = MutableLiveData<NetworkResult<User>>()
    val memberLiveData : LiveData<NetworkResult<User>>
        get() = _memberLiveData

    suspend fun getMembers(accountId: String) {
        _membersLiveData.postValue(NetworkResult.Loading())
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
                _membersLiveData.postValue(NetworkResult.Success(memberList.sortedBy { it.name }))

                Log.d(TAG, "MemberList $memberList")
            } catch (e: Exception) {
                Log.e(TAG, "Exception: $e")
            }
        }else if(response.errorBody()!=null){
            _membersLiveData.postValue(NetworkResult.Error("Something went wrong"))
        }else{
            _membersLiveData.postValue(NetworkResult.Error("Something went wrong"))
        }
    }

    suspend fun getMember(uid:String){
        _memberLiveData.postValue(NetworkResult.Loading())
        val response = apiService.getMember(uid)
        if(response.isSuccessful && response.body()!=null){
            val user: User = response.body()!!
            _memberLiveData.postValue(NetworkResult.Success(user))
        }else{
            _memberLiveData.postValue(NetworkResult.Error("Something went wrong"))
        }
    }
}