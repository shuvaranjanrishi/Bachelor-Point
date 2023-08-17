package com.therishideveloper.bachelorpoint.listener

import com.therishideveloper.bachelorpoint.model.ExpenseClosing

interface ExpenseClosingListener {
    fun onChangeExpense(mealList: List<ExpenseClosing>)
}