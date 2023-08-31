package com.therishideveloper.bachelorpoint.ui.signInSignUp

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.therishideveloper.bachelorpoint.R
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

    private val TAG = "AuthRepo"

    private val _singInResponseLiveData = MutableLiveData<NetworkResult<String>>()
    val singInResponseLiveData: LiveData<NetworkResult<String>>
        get() = _singInResponseLiveData

    private val _singUpResponseLiveData = MutableLiveData<NetworkResult<User>>()
    val singUpResponseLiveData: LiveData<NetworkResult<User>>
        get() = _singUpResponseLiveData

    fun signIn(
        email: String,
        password: String,
        auth: FirebaseAuth
    ) {
        _singInResponseLiveData.postValue(NetworkResult.Loading())

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    _singInResponseLiveData.postValue(NetworkResult.Success(auth.currentUser!!.uid))
                } else {
                    Log.e(TAG, "addOnCompleteListener" + it.exception)
                }
            }
            .addOnFailureListener {
                _singInResponseLiveData.postValue(NetworkResult.Error(it.message))
            }
    }

    fun signUp(
        name: String,
        email: String,
        password: String,
        address: String,
        phone: String,
        auth: FirebaseAuth,
        database: DatabaseReference
    ) {
        _singUpResponseLiveData.postValue(NetworkResult.Loading())

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val uid = auth.currentUser!!.uid
                    saveUserInfo(uid,name,email,password,address,phone,auth,database)
                } else {
                    _singUpResponseLiveData.postValue(NetworkResult.Error(it.exception.toString()))
                }
            }
            .addOnFailureListener {
                _singUpResponseLiveData.postValue(NetworkResult.Error(it.message.toString()))
            }
    }

    private fun saveUserInfo(
        uid: String,
        name: String,
        email: String,
        password: String,
        address: String,
        phone: String,
        auth:FirebaseAuth,
        database: DatabaseReference
    ) {
        val timestamp = "" + System.currentTimeMillis()
        val user =
            User(
                "" + timestamp,
                "" + uid,
                "" + uid,
                "" + name,
                "" + phone,
                "" + address,
                "" + email,
                "" + password,
                "Admin",
                "true",
                "" + timestamp,
                "" + timestamp
            )
        database.child("Users").child(uid).setValue(user)
            .addOnCompleteListener {
                database.child("Accounts").child(auth.uid.toString()).child("Members").child(uid)
                    .setValue(user)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            _singUpResponseLiveData.postValue(NetworkResult.Success(user))
                        } else {
                            _singUpResponseLiveData.postValue(NetworkResult.Error(it.exception.toString()))
                        }
                    }
            }
    }

}