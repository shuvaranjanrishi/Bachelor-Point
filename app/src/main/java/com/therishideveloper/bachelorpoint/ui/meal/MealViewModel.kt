package com.therishideveloper.bachelorpoint.ui.meal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.therishideveloper.bachelorpoint.api.NetworkResult
import com.therishideveloper.bachelorpoint.model.Meal
import com.therishideveloper.bachelorpoint.model.User
import com.therishideveloper.bachelorpoint.ui.member.MemberRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MealViewModel @Inject constructor(private val mealRepo: MealRepo) : ViewModel() {

    var mealList: List<Meal>

    val mealsLiveData : LiveData<NetworkResult<List<Meal>>>
        get() = mealRepo.mealsLiveData

    private val _data = MutableLiveData<List<Meal>>().apply {

        val timestamp: String = "" + System.currentTimeMillis();

        mealList = listOf(
            Meal(
                "1",
                "1",
                "Shuva Ranjan Rishi",
                "1",
                "1",
                "1",
                "3",
                timestamp,
                timestamp
            ),
            Meal(
                "1",
                "1",
                "Shuva Ranjan",
                "1",
                "1",
                "0",
                "2",
                timestamp,
                timestamp
            ),
            Meal(
                "1",
                "1",
                "Shuva Rishi",
                "0",
                "1",
                "1",
                "2",
                timestamp,
                timestamp
            ),
            Meal(
                "1",
                "1",
                "Shuva Rishi",
                "0",
                "1",
                "1",
                "2",
                timestamp,
                timestamp
            ),
            Meal(
                "1",
                "1",
                "Shuva Rishi",
                "0",
                "1",
                "1",
                "2",
                timestamp,
                timestamp
            ),
            Meal(
                "1",
                "1",
                "Shuva Rishi",
                "0",
                "1",
                "1",
                "2",
                timestamp,
                timestamp
            ),
        )

        value = mealList
    }
    val data: LiveData<List<Meal>> = _data

    fun getMealListOfADay(accountId: String,monthAndYear: String, date: String) {
        viewModelScope.launch {
            mealRepo.getMealListOfADay(accountId,monthAndYear,date)
        }
    }

    fun getMealListOfAMonth(accountId: String,monthAndYear: String) {
        viewModelScope.launch {
            mealRepo.getMealListOfAMonth(accountId,monthAndYear)
        }
    }
}