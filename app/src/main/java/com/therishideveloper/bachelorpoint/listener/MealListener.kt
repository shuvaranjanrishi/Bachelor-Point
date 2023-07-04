package com.therishideveloper.bachelorpoint.listener

import com.therishideveloper.bachelorpoint.model.Meal

interface MealListener {
    fun onChangeMeal(mealList: List<Meal>)
}