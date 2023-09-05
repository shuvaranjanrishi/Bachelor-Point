package com.therishideveloper.bachelorpoint.ui.meal

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.therishideveloper.bachelorpoint.api.ApiService
import com.therishideveloper.bachelorpoint.api.NetworkResult
import com.therishideveloper.bachelorpoint.model.Meal
import org.json.JSONObject
import javax.inject.Inject

/**
 * Created by Shuva Ranjan Rishi on 27,August,2023
 * BABL, Bangladesh,
 */

class MealRepo @Inject constructor(private val apiService: ApiService) {

    private val TAG = "MemberRepo"

    private val _mealsLiveData = MutableLiveData<NetworkResult<List<Meal>>>()
    val mealsLiveData: LiveData<NetworkResult<List<Meal>>>
        get() = _mealsLiveData

    suspend fun getMealListOfADay(accountId: String, monthAndYear: String, date: String) {
        _mealsLiveData.postValue(NetworkResult.Loading())
        try {
            val response = apiService.getMealListOfADay(accountId, monthAndYear, date)

            if (response.isSuccessful && response.body() != null) {

                val jsonObject = JSONObject(response.body()!!.asJsonObject.toString())
                val mealList: MutableList<Meal> = mutableListOf()
                val keys: Iterator<Any> = jsonObject.keys()
                while (keys.hasNext()) {
                    val key = keys.next().toString()
                    val childObj: JSONObject = jsonObject.getJSONObject(key)

                    mealList.add(
                        Meal(
                            "" + childObj.getString("id"),
                            "" + childObj.getString("memberId"),
                            "" + childObj.getString("name"),
                            "" + childObj.getString("firstMeal"),
                            "" + childObj.getString("secondMeal"),
                            "" + childObj.getString("thirdMeal"),
                            "" + childObj.getString("subTotalMeal"),
                            "" + childObj.getString("monthAndYear"),
                            "" + childObj.getString("date"),
                            "" + childObj.getString("createdAt"),
                            "" + childObj.getString("updatedAt")
                        )
                    )
                }
                _mealsLiveData.postValue(NetworkResult.Success(mealList.sortedBy { it.name }))

            } else if (response.errorBody() != null) {
                _mealsLiveData.postValue(NetworkResult.Error("Something went wrong"))
            } else {
                _mealsLiveData.postValue(NetworkResult.Error("Something went wrong"))
            }
        } catch (e: Exception) {
            _mealsLiveData.postValue(NetworkResult.Error(e.localizedMessage))
        }
    }

    suspend fun getMealListOfAMonth(accountId: String, monthAndYear: String) {
        _mealsLiveData.postValue(NetworkResult.Loading())
        try {
            val response = apiService.getMealListOfAMonth(accountId, monthAndYear)

            if (response.isSuccessful && response.body() != null) {

                val jsonObject = JSONObject(response.body()!!.asJsonObject.toString())
                val mealList: MutableList<Meal> = mutableListOf()
                val keys: Iterator<Any> = jsonObject.keys()
                while (keys.hasNext()) {
                    val key = keys.next().toString()
                    val childObj: JSONObject = jsonObject.getJSONObject(key)

                    mealList.add(
                        Meal(
                            "" + childObj.getString("id"),
                            "" + childObj.getString("memberId"),
                            "" + childObj.getString("name"),
                            "" + childObj.getString("firstMeal"),
                            "" + childObj.getString("secondMeal"),
                            "" + childObj.getString("thirdMeal"),
                            "" + childObj.getString("subTotalMeal"),
                            "" + childObj.getString("monthAndYear"),
                            "" + childObj.getString("date"),
                            "" + childObj.getString("createdAt"),
                            "" + childObj.getString("updatedAt")
                        )
                    )
                }
                _mealsLiveData.postValue(NetworkResult.Success(mealList.sortedBy { it.name }))

            } else if (response.errorBody() != null) {
                _mealsLiveData.postValue(NetworkResult.Error("Something went wrong"))
            } else {
                _mealsLiveData.postValue(NetworkResult.Error("Something went wrong"))
            }
        } catch (e: Exception) {
            _mealsLiveData.postValue(NetworkResult.Error(e.localizedMessage))
        }
    }

}