package com.therishideveloper.bachelorpoint.ui.rent

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.therishideveloper.bachelorpoint.api.ApiService
import com.therishideveloper.bachelorpoint.api.NetworkResult
import com.therishideveloper.bachelorpoint.model.Rent
import com.therishideveloper.bachelorpoint.model.SeparateRent
import javax.inject.Inject

/**
 * Created by Shuva Ranjan Rishi on 27,August,2023
 * BABL, Bangladesh,
 */

class RentRepo @Inject constructor(private val apiService: ApiService) {

    private val TAG = "MemberRepo"

    private val _rentLiveData = MutableLiveData<NetworkResult<List<Rent>>>()
    val rentLiveData get() = _rentLiveData

    private val _separateRentLiveData = MutableLiveData<NetworkResult<List<SeparateRent>>>()
    val separateRentLiveData get() = _separateRentLiveData

    fun getBills(billRef: DatabaseReference) {
        _rentLiveData.postValue(NetworkResult.Loading())
        billRef.addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val rentList: MutableList<Rent> = mutableListOf()
                    for (ds in dataSnapshot.children) {
                        val rent: Rent? = ds.getValue(Rent::class.java)
                        rentList.add(rent!!)
                    }
                    _rentLiveData.postValue(NetworkResult.Success(rentList.sortedBy { it.amount!!.toInt() }))
                }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e(TAG, "DatabaseError", error.toException())
                        _rentLiveData.postValue(NetworkResult.Error(error.message))
                    }
                }
            )
    }

    fun getSeparateRentList(rentRef: DatabaseReference) {
        _separateRentLiveData.postValue(NetworkResult.Loading())
        rentRef.addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val separateRentList: MutableList<SeparateRent> = mutableListOf()
                        for (ds in dataSnapshot.children) {
                            val separateRent: SeparateRent? = ds.getValue(SeparateRent::class.java)
                            separateRentList.add(separateRent!!)
                        }
                        _separateRentLiveData.postValue(NetworkResult.Success(separateRentList.sortedBy { it.name }))
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e(TAG, "DatabaseError", error.toException())
                        _separateRentLiveData.postValue(NetworkResult.Error(error.message))
                    }
                }
            )
    }

}