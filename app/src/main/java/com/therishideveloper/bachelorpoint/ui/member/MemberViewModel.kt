package com.therishideveloper.bachelorpoint.ui.member

import android.util.Log
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
import com.therishideveloper.bachelorpoint.model.User

class MemberViewModel : ViewModel() {

    private val TAG = "MemberViewModel"

    private var auth: FirebaseAuth = Firebase.auth
    private var database: DatabaseReference =
        Firebase.database.reference.child("Bachelor Point").child("Users")

    private val _data = MutableLiveData<List<User>>().apply {

        val accountId: String = auth.uid!!
        database.child(accountId).child("Members")
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val memberList: MutableList<User> = mutableListOf()
                        for (ds in dataSnapshot.children) {
                            val user: User? = ds.getValue(User::class.java)
                            memberList.add(user!!)
                        }
                        value = memberList.sortedBy { it.name }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e(TAG, "DatabaseError", error.toException())
                    }
                }
            )

    }
    val data: LiveData<List<User>> = _data
}