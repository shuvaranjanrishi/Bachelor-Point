package com.therishideveloper.bachelorpoint.ui.member

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MemberViewModel @Inject constructor(private val memberRepo: MemberRepo) : ViewModel() {

    private val TAG = "MemberViewModel"

    private var database: DatabaseReference =
        Firebase.database.reference.child("Bachelor Point").child("Users")

    fun getMembers(accountId: String) {
        viewModelScope.launch {
            memberRepo.getMembers(database, accountId)
        }
    }

//    private val _data = MutableLiveData<List<User>>().apply {
//        val session: SharedPreferences =
//            context.getSharedPreferences("UserSession", Context.MODE_PRIVATE)
//
////        val accountId: String = auth.uid!!
//        val accountId = session.getString("ACCOUNT_ID", "").toString()
//        database.child(accountId).child("Members")
//            .addListenerForSingleValueEvent(
//                object : ValueEventListener {
//                    override fun onDataChange(dataSnapshot: DataSnapshot) {
//                        val memberList: MutableList<User> = mutableListOf()
//                        for (ds in dataSnapshot.children) {
//                            val user: User? = ds.getValue(User::class.java)
//                            memberList.add(user!!)
//                        }
//
//                        value = memberList.sortedBy { it.name }
//                    }
//
//                    override fun onCancelled(error: DatabaseError) {
//                        Log.e(TAG, "DatabaseError", error.toException())
//                    }
//                }
//            )
//
//    }
//    val data: LiveData<List<User>> = _data
}