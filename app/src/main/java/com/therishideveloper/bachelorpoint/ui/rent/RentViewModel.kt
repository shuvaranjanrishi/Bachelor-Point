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
                "",
                "",
                timestamp,
                timestamp
            ),
            Rent(
                "2",
                "Electricity Bill",
                "",
                "",
                timestamp,
                timestamp
            ),
            Rent(
                "3",
                "Service Charge",
                "",
                "",
                timestamp,
                timestamp
            ),
            Rent(
                "4",
                "Water Bill",
                "",
                "",
                timestamp,
                timestamp
            ),
            Rent(
                "5",
                "Khala Bill",
                "",
                "",
                timestamp,
                timestamp
            ),
        )

        value = rentList
    }
    val data: LiveData<List<Rent>> = _data
}