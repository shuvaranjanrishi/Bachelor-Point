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

    fun getMembers(accountId: String) {
        viewModelScope.launch {
            memberRepo.getMembers(accountId)
        }
    }
}