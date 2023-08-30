package com.therishideveloper.bachelorpoint.ui.expense

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.therishideveloper.bachelorpoint.api.NetworkResult
import com.therishideveloper.bachelorpoint.model.Expense
import com.therishideveloper.bachelorpoint.model.User
import com.therishideveloper.bachelorpoint.ui.signInSignUp.AuthRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExpenseViewModel @Inject constructor(private val expenseRepo: ExpenseRepo): ViewModel() {

    private val TAG = "ExpenditureViewModel"

    val expenseResponseLiveData : LiveData<NetworkResult<List<Expense>>>
        get() = expenseRepo.expenseResponseLiveData

    fun getExpenses(
        monthAndYear: String,
        accountId: String,
        database: DatabaseReference
    ) {
        viewModelScope.launch {
            expenseRepo.getExpenses(monthAndYear, accountId, database)
        }
    }
}