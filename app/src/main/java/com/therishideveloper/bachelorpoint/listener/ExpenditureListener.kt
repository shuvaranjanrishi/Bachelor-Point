package com.therishideveloper.bachelorpoint.listener

import com.therishideveloper.bachelorpoint.model.Expense

interface ExpenditureListener {
    fun onChangeExpenditure(expenditureList: List<Expense>)
}