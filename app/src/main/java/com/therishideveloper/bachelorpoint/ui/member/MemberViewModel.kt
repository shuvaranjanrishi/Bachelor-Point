package com.therishideveloper.bachelorpoint.ui.member

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.therishideveloper.bachelorpoint.api.NetworkResult
import com.therishideveloper.bachelorpoint.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MemberViewModel @Inject constructor(private val memberRepo: MemberRepo) : ViewModel() {

    private val TAG = "MemberViewModel"

    val membersLiveData get() = memberRepo.membersLiveData

    val memberLiveData : LiveData<NetworkResult<User>>
        get() = memberRepo.memberLiveData

    fun getMembers(accountId: String) {
        viewModelScope.launch {
            memberRepo.getMembers(accountId)
        }
    }

    fun getMember(uid: String) {
        viewModelScope.launch {
            memberRepo.getMember(uid)
        }
    }
}