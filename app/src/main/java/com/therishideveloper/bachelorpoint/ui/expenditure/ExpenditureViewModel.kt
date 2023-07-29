package com.therishideveloper.bachelorpoint.ui.expenditure

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.therishideveloper.bachelorpoint.model.Expenditure
import kotlin.coroutines.coroutineContext

class ExpenditureViewModel : ViewModel() {

    private val TAG = "ExpenditureViewModel"

    private var auth: FirebaseAuth = Firebase.auth
    private var database: DatabaseReference = Firebase.database.reference.child("Bachelor Point").child("Users")
//    private var session: SharedPreferences = requireContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE)


    private val _data = MutableLiveData<List<Expenditure>>().apply {

//        val accountId: String = "N8NevFOQ3kUnC0VyZdk90rrMaV82";
        val accountId: String = auth.uid!!
        database.child(accountId).child("Expenditure")
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val dataList: MutableList<Expenditure> = mutableListOf()
                        for (ds in dataSnapshot.children) {
                            val expenditure: Expenditure? = ds.getValue(Expenditure::class.java)
                            dataList.add(expenditure!!)
                        }
                        value = dataList
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e(TAG, "DatabaseError", error.toException())
                    }
                }
            )
    }
    val data: LiveData<List<Expenditure>> = _data
}