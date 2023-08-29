package com.therishideveloper.bachelorpoint.ui.signInSignUp

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.therishideveloper.bachelorpoint.api.ApiService
import com.therishideveloper.bachelorpoint.api.NetworkResult
import com.therishideveloper.bachelorpoint.model.User
import org.json.JSONObject
import javax.inject.Inject

/**
 * Created by Shuva Ranjan Rishi on 27,August,2023
 * BABL, Bangladesh,
 */

class AuthRepo @Inject constructor(private val apiService: ApiService) {

    private val TAG = "MemberRepo"

    private val _singInResponseLiveData = MutableLiveData<NetworkResult<User>>()
    val singInResponseLiveData: LiveData<NetworkResult<User>>
        get() = _singInResponseLiveData

    fun signIn(
        email: String,
        password: String,
        auth: FirebaseAuth,
        database: DatabaseReference
    ) {
        _singInResponseLiveData.postValue(NetworkResult.Loading())

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val userid = auth.currentUser!!.uid
                    getUserInfo(database,userid)
                } else {
                    Log.e(TAG, "addOnCompleteListener" + it.exception)
                }
            }
            .addOnFailureListener {
                _singInResponseLiveData.postValue(NetworkResult.Error(it.message))
            }
    }

    private fun getUserInfo(database: DatabaseReference, uid: String) {
        database.child("Users").orderByChild("uid").equalTo(uid)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (ds in dataSnapshot.children) {
                            val user: User? = ds.getValue<User>()
                            if (user != null) {
                                Log.e(TAG, "LoginUser: $user")
                                _singInResponseLiveData.postValue(NetworkResult.Success(user))
                            } else {
                                _singInResponseLiveData.postValue(NetworkResult.Error("User Not Found!"))
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e(TAG, "getUserInfo" + error.toException())
                    }
                }
            )
    }

}