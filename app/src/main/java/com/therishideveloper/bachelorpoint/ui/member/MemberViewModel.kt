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
import com.therishideveloper.bachelorpoint.model.Meal
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
                        value = memberList
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e(TAG, "DatabaseError", error.toException())
                    }
                }
            )

    }
    val data: LiveData<List<User>> = _data

//    private val _member = MutableLiveData<User>().apply {
//
//        val accountId: String = auth.uid!!
//        database.orderByChild("uid").equalTo(accountId)
//            .addListenerForSingleValueEvent(
//                object : ValueEventListener {
//                    override fun onDataChange(dataSnapshot: DataSnapshot) {
//                        var member: User? = null
//                        for (ds in dataSnapshot.children) {
//                            member = ds.getValue(User::class.java)!!
//                            Log.d(TAG, "onDataChange: $member")
//                        }
//                        value = member
//                    }
//
//                    override fun onCancelled(error: DatabaseError) {
//                        Log.e(TAG, "DatabaseError", error.toException())
//                    }
//                }
//            )
//
//    }
//    val member: LiveData<User> = _member

//    private val _mealList = MutableLiveData<List<Meal>>().apply {
//
//        val accountId: String = auth.uid!!
//        database.orderByChild("uid").equalTo(accountId)
//            .addListenerForSingleValueEvent(
//                object : ValueEventListener {
//                    override fun onDataChange(dataSnapshot: DataSnapshot) {
//                        val mealList: MutableList<Meal> = mutableListOf()
//                        for (ds in dataSnapshot.children) {
//                            val user: User? = ds.getValue(User::class.java)
//                            user!!
//                            val meal = Meal(
//                                ""+System.currentTimeMillis(),
//                                ""+ user.id,
//                                ""+ user.name,
//                                "0",
//                                "0",
//                                "0",
//                                "0",
//                                ""+System.currentTimeMillis(),
//                                ""+System.currentTimeMillis()
//                            )
//                            mealList.add(meal)
//                            Log.d(TAG, "onDataChange: mealList: $mealList")
//                        }
//                        value = mealList
//                    }
//
//                    override fun onCancelled(error: DatabaseError) {
//                        Log.e(TAG, "DatabaseError", error.toException())
//                    }
//                }
//            )
//
//    }
//    val mealList: LiveData<List<Meal>> = _mealList

}