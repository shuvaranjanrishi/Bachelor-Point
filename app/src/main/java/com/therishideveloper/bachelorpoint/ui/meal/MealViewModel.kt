package com.therishideveloper.bachelorpoint.ui.meal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.therishideveloper.bachelorpoint.model.Meal
import com.therishideveloper.bachelorpoint.model.Member

class MealViewModel : ViewModel() {

    var mealList: List<Meal>

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
}