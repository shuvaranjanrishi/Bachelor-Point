package com.therishideveloper.bachelorpoint.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.therishideveloper.bachelorpoint.R
import com.therishideveloper.bachelorpoint.model.Module

class HomeViewModel : ViewModel() {

    var moduleList: List<Module>

    private val _data = MutableLiveData<List<Module>>().apply {

        moduleList = listOf(
            Module(R.drawable.shopping, "বাজার"),
            Module(R.drawable.meal, "মিল"),
            Module(R.drawable.rent_and_bill, "ভাড়া ও বিল"),
            Module(R.drawable.account, "মাসিক হিসাব"),
            Module(R.drawable.members, "মেম্বার্স"),
            Module(R.drawable.logout, "লগ আউট"),
        )

        value = moduleList
    }
    val data: LiveData<List<Module>> = _data
}