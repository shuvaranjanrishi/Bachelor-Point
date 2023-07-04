package com.therishideveloper.bachelorpoint.listener

import com.therishideveloper.bachelorpoint.model.Rent

interface RentListener {
    fun onChangeRent(rentList: List<Rent>)
}