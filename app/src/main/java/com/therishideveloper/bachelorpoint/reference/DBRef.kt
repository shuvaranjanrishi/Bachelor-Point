package com.therishideveloper.bachelorpoint.reference

import android.content.Context
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.therishideveloper.bachelorpoint.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * Created by Shuva Ranjan Rishi on 06,July,2023
 * BABL, Bangladesh,
 */

class DBRef @Inject constructor(@ApplicationContext context: Context) {

    private var dbRef = Firebase.database.reference.child(context.getString(R.string.database_name))
    private var accountRef = dbRef.child("Accounts")

    fun getDbRef(): DatabaseReference {
        return dbRef
    }

    fun getAccountRef(): DatabaseReference {
        return accountRef
    }

    fun getRentRef(accountId: String): DatabaseReference {
        return accountRef.child(accountId).child("RentAndBill").child("Rent")
    }

    fun getBillRef(accountId: String): DatabaseReference {
        return accountRef.child(accountId).child("RentAndBill").child("Bill")
    }
}
