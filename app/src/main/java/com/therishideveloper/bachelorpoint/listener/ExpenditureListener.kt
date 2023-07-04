package com.therishideveloper.bachelorpoint.listener

import com.therishideveloper.bachelorpoint.model.Expenditure

interface ExpenditureListener {
    fun onChangeExpenditure(expenditureList: List<Expenditure>)
}