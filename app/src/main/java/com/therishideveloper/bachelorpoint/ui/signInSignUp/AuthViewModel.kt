package com.therishideveloper.bachelorpoint.ui.signInSignUp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.therishideveloper.bachelorpoint.api.NetworkResult
import com.therishideveloper.bachelorpoint.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val authRepo: AuthRepo) : ViewModel() {

    private val TAG = "AuthViewModel"

    val singInResponseLiveData : LiveData<NetworkResult<User>>
        get() = authRepo.singInResponseLiveData
    val singUpResponseLiveData : LiveData<NetworkResult<User>>
        get() = authRepo.singUpResponseLiveData

    fun signIn(
        email: String,
        password: String,
        auth: FirebaseAuth,
        database: DatabaseReference
    ) {
        viewModelScope.launch {
            authRepo.signIn(email, password, auth, database)
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
        viewModelScope.launch {
            authRepo.signUp(name,email, password,address,phone, auth, database)
        }
    }
}