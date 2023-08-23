package com.therishideveloper.bachelorpoint.listener

import com.therishideveloper.bachelorpoint.model.Rent
import com.therishideveloper.bachelorpoint.model.SeparateRent

interface SeparateRentListener {
    fun onChangeSeparateRent(total1:String,total2:String,total3: String)
}