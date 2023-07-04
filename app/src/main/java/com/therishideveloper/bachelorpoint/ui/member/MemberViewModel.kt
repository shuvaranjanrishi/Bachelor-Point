package com.therishideveloper.bachelorpoint.ui.member

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.therishideveloper.bachelorpoint.R
import com.therishideveloper.bachelorpoint.model.Member
import com.therishideveloper.bachelorpoint.model.Module

class MemberViewModel : ViewModel() {

    var memberList: List<Member>

    private val _data = MutableLiveData<List<Member>>().apply {

        val timestamp: String = "" + System.currentTimeMillis();

        memberList = listOf(
            Member(
                "1",
                "Shuvo",
                "shuvo@gmail.com",
                "Shahjadpur",
                "01738558244",
                timestamp,
                timestamp
            ),
            Member(
                "2",
                "Shimul",
                "shimul@gmail.com",
                "Shahjadpur",
                "01973775235",
                timestamp,
                timestamp
            ),
            Member(
                "3",
                "Gopal",
                "gopal@gmail.com",
                "Shahjadpur",
                "01734895431",
                timestamp,
                timestamp
            ),
            Member(
                "4",
                "Sajib",
                "sajib@gmail.com",
                "Shahjadpur",
                "01860231340",
                timestamp,
                timestamp
            ),
            Member(
                "5",
                "Bollov",
                "bollob@gmail.com",
                "Shahjadpur",
                "01765678044",
                timestamp,
                timestamp
            ),
            Member(
                "6",
                "Choyon",
                "choyon@gmail.com",
                "Shahjadpur",
                "01735505058",
                timestamp,
                timestamp
            ),
        )

        value = memberList
    }
    val data: LiveData<List<Member>> = _data
}