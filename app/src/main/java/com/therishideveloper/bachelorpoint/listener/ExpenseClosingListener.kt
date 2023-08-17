package com.therishideveloper.bachelorpoint.listener

import com.therishideveloper.bachelorpoint.model.ExpenseClosing
import com.therishideveloper.bachelorpoint.model.MealClosing

interface ExpenseClosingListener {
    fun onChangeExpense(totalCostOfMeal:String,totalResult:String)
}