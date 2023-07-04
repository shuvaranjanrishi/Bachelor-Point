package com.therishideveloper.bachelorpoint.ui.expenditure

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.therishideveloper.bachelorpoint.model.Expenditure
import com.therishideveloper.bachelorpoint.model.Meal
import com.therishideveloper.bachelorpoint.model.Member

class ExpenditureViewModel : ViewModel() {

    var expenditureList: List<Expenditure>

    private val _data = MutableLiveData<List<Expenditure>>().apply {

        val timestamp: String = "" + System.currentTimeMillis();

        expenditureList = listOf(
            Expenditure(
                "1",
                "10-06-23",
                "1",
                "Shuva Rishi",
                "500",
                timestamp,
                timestamp
            ),
            Expenditure(
                "1",
                "10-06-23",
                "1",
                "Shuva Rishi",
                "700",
                timestamp,
                timestamp
            ),
            Expenditure(
                "1",
                "10-06-23",
                "3",
                "Shuva Ranjan Rishi",
                "250",
                timestamp,
                timestamp
            ),
            Expenditure(
                "1",
                "10-06-23",
                "2",
                "Shuva Ranjan",
                "300",
                timestamp,
                timestamp
            ),
            Expenditure(
                "1",
                "10-06-23",
                "2",
                "Shuva Ranjan",
                "400",
                timestamp,
                timestamp
            ),
        )

        value = expenditureList
    }
    val data: LiveData<List<Expenditure>> = _data
}