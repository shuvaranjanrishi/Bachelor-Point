package com.therishideveloper.bachelorpoint.utils

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

object Reference {

    val databaseRef: DatabaseReference
//        get() = FirebaseDatabase.getInstance().getReference("BachelorPoint").child("Users")
        get() = Firebase.database.reference.child("BachelorPoint").child("Users")
}