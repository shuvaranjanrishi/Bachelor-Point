package com.therishideveloper.bachelorpoint.listener

import com.therishideveloper.bachelorpoint.model.Expense

interface ExpenseListener {
    fun onChangeExpense(expenseList: List<Expense>)
}