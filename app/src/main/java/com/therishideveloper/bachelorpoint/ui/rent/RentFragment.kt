package com.therishideveloper.bachelorpoint.ui.rent

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.therishideveloper.bachelorpoint.R
import com.therishideveloper.bachelorpoint.adapter.RentAdapter
import com.therishideveloper.bachelorpoint.databinding.FragmentRentBinding
import com.therishideveloper.bachelorpoint.listener.MyMonthAndYear
import com.therishideveloper.bachelorpoint.listener.RentListener
import com.therishideveloper.bachelorpoint.model.Rent
import com.therishideveloper.bachelorpoint.utils.MyCalender


class RentFragment : Fragment(), RentListener {

    private val TAG = "RentFragment"

    private var _binding: FragmentRentBinding? = null
    private val binding get() = _binding!!

    private lateinit var database: DatabaseReference
    private lateinit var session: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRentBinding.inflate(inflater, container, false)

        database = Firebase.database.reference.child(getString(R.string.app_name)).child("Users")
        session = requireContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.dateTv.text = MyCalender.currentMonthYear
        getRentAndBill(MyCalender.currentMonthYear)
        binding.dateTv.setOnClickListener {
            MyCalender.pickMonthAndYear(activity, object : MyMonthAndYear {
                override fun onPickMonthAndYear(monthAndYear: String?) {
                    binding.dateTv.text = monthAndYear
                    if (monthAndYear != null) {
                        getRentAndBill(monthAndYear)
                    }
                    Log.d(TAG, "monthAndYear: $monthAndYear")
                }

                override fun onPickDate(date: String?) {
                    Log.d(TAG, "date: $date")
                }
            })
        }
    }

    private fun getRentAndBill(monthAndYear: String?) {

        val accountId = session.getString("ACCOUNT_ID", "").toString()
        val listener = this

        database.child(accountId).child("RentAndBill")
            .orderByChild("monthAndYear")
            .equalTo(monthAndYear)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val rentList: MutableList<Rent> = mutableListOf()
                        for (ds in dataSnapshot.children) {
                            val rent: Rent? = ds.getValue(Rent::class.java)
                            rentList.add(rent!!)
                        }
                        val adapter = RentAdapter(listener, rentList.sortedBy { it.amount!!.toInt() })
                        binding.recyclerView.adapter = adapter
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e(TAG, "DatabaseError", error.toException())
                    }
                }
            )
    }

    override fun onChangeRent(rentList: List<Rent>) {
        if (rentList.isNotEmpty()) {
            var totalAmount = 0
            val totalMember = 6
            for (rent in rentList) {
                totalAmount += rent.amount!!.toInt()
            }
            val costPerHead: Int = totalAmount / totalMember
            binding.totalAmountTv.text = totalAmount.toString()
            binding.totalMemberTv.text = totalMember.toString()
            binding.costPerHeadTv.text = costPerHead.toString()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}