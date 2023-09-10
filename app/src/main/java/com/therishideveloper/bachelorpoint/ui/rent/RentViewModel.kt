package com.therishideveloper.bachelorpoint.ui.rent

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DatabaseReference
import com.therishideveloper.bachelorpoint.model.Rent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RentViewModel @Inject constructor(private val rentRepo: RentRepo) : ViewModel() {

    val rentLiveData get() = rentRepo.rentLiveData
    val separateRentLiveData get() = rentRepo.separateRentLiveData

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
                "Gas Bill",
                "",
                "",
                timestamp,
                timestamp
            ),
            Rent(
                "4",
                "Internet Bill",
                "",
                "",
                timestamp,
                timestamp
            ),
            Rent(
                "5",
                "Service Charge",
                "",
                "",
                timestamp,
                timestamp
            ),
            Rent(
                "6",
                "Water Bill",
                "",
                "",
                timestamp,
                timestamp
            ),
            Rent(
                "7",
                "Khala Bill",
                "",
                "",
                timestamp,
                timestamp
            ),
            Rent(
                "8",
                "Garbage Cleaning Bill",
                "",
                "",
                timestamp,
                timestamp
            ),
            Rent(
                "9",
                "Others",
                "",
                "",
                timestamp,
                timestamp
            ),
        )

        value = rentList
    }
    val data: LiveData<List<Rent>> = _data

    fun getRentAndBill(accountId: String, monthAndYear: String, database: DatabaseReference) {
        viewModelScope.launch {
            rentRepo.getRentAndBill(accountId, monthAndYear, database)
        }
    }

    fun getSeparateRentList(accountId: String, monthAndYear: String, database: DatabaseReference) {
        viewModelScope.launch {
            rentRepo.getSeparateRentList(accountId, monthAndYear, database)
        }
    }

}