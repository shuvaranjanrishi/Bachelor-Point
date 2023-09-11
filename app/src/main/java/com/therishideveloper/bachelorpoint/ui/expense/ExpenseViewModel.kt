package com.therishideveloper.bachelorpoint.ui.expense

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DatabaseReference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExpenseViewModel @Inject constructor(private val expenseRepo: ExpenseRepo): ViewModel() {

    private val TAG = "ExpenditureViewModel"

    val expenseResponseLiveData get() = expenseRepo.expenseResponseLiveData

    fun getExpenses(expenseRef: DatabaseReference) {
        viewModelScope.launch {
            expenseRepo.getExpenses(expenseRef)
        }
    }
}