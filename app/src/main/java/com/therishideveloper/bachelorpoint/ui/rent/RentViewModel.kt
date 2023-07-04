package com.therishideveloper.bachelorpoint.ui.rent

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.therishideveloper.bachelorpoint.model.Meal
import com.therishideveloper.bachelorpoint.model.Member
import com.therishideveloper.bachelorpoint.model.Rent

class RentViewModel : ViewModel() {

    var rentList: List<Rent>

    private val _data = MutableLiveData<List<Rent>>().apply {

        val timestamp: String = "" + System.currentTimeMillis();

        rentList = listOf(
            Rent(
                "1",
                "House Rent",
                "14000",
                timestamp,
                timestamp
            ),
            Rent(
                "1",
                "Electricity Bill",
                "1000",
                timestamp,
                timestamp
            ),
            Rent(
                "1",
                "Service Charge",
                "500",
                timestamp,
                timestamp
            ),
            Rent(
                "1",
                "Water Bill",
                "500",
                timestamp,
                timestamp
            ),
            Rent(
                "1",
                "Khala Bill",
                "3600",
                timestamp,
                timestamp
            ),
        )

        value = rentList
    }
    val data: LiveData<List<Rent>> = _data
}