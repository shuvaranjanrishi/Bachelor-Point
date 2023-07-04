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
            Module(R.drawable.baseline_person_24, "বাজার"),
            Module(R.drawable.baseline_person_24, "মিল"),
            Module(R.drawable.baseline_person_24, "ভাড়া ও বিল"),
            Module(R.drawable.baseline_person_24, "মেম্বার্স"),
        )

        value = moduleList
    }
    val data: LiveData<List<Module>> = _data
}