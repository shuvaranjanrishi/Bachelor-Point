package com.therishideveloper.bachelorpoint.ui.expense

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.therishideveloper.bachelorpoint.api.ApiService
import com.therishideveloper.bachelorpoint.api.NetworkResult
import com.therishideveloper.bachelorpoint.model.Expense
import javax.inject.Inject

/**
 * Created by Shuva Ranjan Rishi on 27,August,2023
 * BABL, Bangladesh,
 */

class ExpenseRepo @Inject constructor(private val apiService: ApiService) {

    private val TAG = "AuthRepo"

    private val _expenseResponseLiveData = MutableLiveData<NetworkResult<List<Expense>>>()
    val expenseResponseLiveData get() = _expenseResponseLiveData

    fun getExpenses(
        monthAndYear: String,
        accountId: String,
        database: DatabaseReference
    ) {
        _expenseResponseLiveData.postValue(NetworkResult.Loading())

        database.child(accountId).child("Expense")
            .orderByChild("monthAndYear")
            .equalTo(monthAndYear)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val dataList: MutableList<Expense> = mutableListOf()
                        for (ds in dataSnapshot.children) {
                            val expenditure: Expense? = ds.getValue(Expense::class.java)
                            dataList.add(expenditure!!)
                        }
                        _expenseResponseLiveData.postValue(NetworkResult.Success(dataList))
                    }

                    override fun onCancelled(error: DatabaseError) {
                        _expenseResponseLiveData.postValue(NetworkResult.Error(error.message))
                    }
                }
            )
    }

}