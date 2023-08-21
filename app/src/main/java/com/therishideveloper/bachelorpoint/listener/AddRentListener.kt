package com.therishideveloper.bachelorpoint.listener

import com.therishideveloper.bachelorpoint.model.SeparateRent

interface AddRentListener {
    fun onChangeRent(rentList: List<SeparateRent>)
}