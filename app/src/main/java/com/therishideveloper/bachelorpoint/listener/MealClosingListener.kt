package com.therishideveloper.bachelorpoint.listener

import com.therishideveloper.bachelorpoint.model.MealClosing

interface MealClosingListener {
    fun onChangeMeal(mealList: List<MealClosing>)
}