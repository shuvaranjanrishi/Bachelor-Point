package com.therishideveloper.bachelorpoint.ui.rent

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.therishideveloper.bachelorpoint.R
import com.therishideveloper.bachelorpoint.adapter.RentAdapter
import com.therishideveloper.bachelorpoint.adapter.SeparateRentAdapter
import com.therishideveloper.bachelorpoint.databinding.FragmentRentBinding
import com.therishideveloper.bachelorpoint.listener.MyMonthAndYear
import com.therishideveloper.bachelorpoint.listener.RentListener
import com.therishideveloper.bachelorpoint.listener.SeparateRentListener
import com.therishideveloper.bachelorpoint.model.Rent
import com.therishideveloper.bachelorpoint.model.SeparateRent
import com.therishideveloper.bachelorpoint.utils.MyCalender
import java.math.RoundingMode
import java.text.DecimalFormat


class RentFragment : Fragment(), RentListener, SeparateRentListener {

    private val TAG = "RentFragment"

    private var _binding: FragmentRentBinding? = null
    private val binding get() = _binding!!

    private lateinit var database: DatabaseReference
    private lateinit var session: SharedPreferences
    private lateinit var monthAndYear: String
    private lateinit var decimalFormat: DecimalFormat

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRentBinding.inflate(inflater, container, false)

        database = Firebase.database.reference.child(getString(R.string.app_name)).child("Users")
        session = requireContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        decimalFormat = DecimalFormat("#.##")
        decimalFormat.roundingMode = RoundingMode.UP

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.dateTv.text = MyCalender.currentMonthYear
        getRentAndBill(MyCalender.currentMonthYear)
        monthAndYear = MyCalender.currentMonthYear
        binding.dateTv.setOnClickListener {
            MyCalender.pickMonthAndYear(activity, object : MyMonthAndYear {
                override fun onPickMonthAndYear(monthAndYear: String?) {
                    binding.dateTv.text = monthAndYear
                    if (monthAndYear != null) {
                        getRentAndBill(monthAndYear)
                        this@RentFragment.monthAndYear = monthAndYear
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

        if (monthAndYear != null) {
            database.child(accountId).child("RentAndBill").child(monthAndYear)
                .addListenerForSingleValueEvent(
                    object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            val rentList: MutableList<Rent> = mutableListOf()
                            for (ds in dataSnapshot.children) {
                                val rent: Rent? = ds.getValue(Rent::class.java)
                                if (!ds.key.equals("SeparateRent")) {
                                    rentList.add(rent!!)
                                    Log.d(TAG, "SeparateRent: " + ds.key)
                                }
//                                else {
//                                    getSeparateRentList(accountId, monthAndYear)
//                                }
                            }
                            val adapter =
                                RentAdapter(listener, rentList.sortedBy { it.amount!!.toInt() })
                            binding.recyclerView1.adapter = adapter
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Log.e(TAG, "DatabaseError", error.toException())
                        }
                    }
                )
        }
    }

    private fun getSeparateRentList(accountId: String, perHeadCost: Double) {
        val listener = this
        database.child(accountId).child("RentAndBill").child(monthAndYear)
            .child("SeparateRent")
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val separateRentList: MutableList<SeparateRent> = mutableListOf()
                        for (ds in dataSnapshot.children) {
                            val separateRent: SeparateRent? = ds.getValue(SeparateRent::class.java)
                            separateRentList.add(separateRent!!)
                        }
                        if (separateRentList.isNotEmpty()) {
                            val adapter =
                                SeparateRentAdapter(
                                    listener,
                                    perHeadCost,
                                    separateRentList.sortedBy { it.name })
                            binding.recyclerView2.adapter = adapter
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e(TAG, "DatabaseError", error.toException())
                    }
                }
            )
    }

    override fun onChangeRent(rentList: List<Rent>) {
        var perHeadCost = 0.0
        if (rentList.isNotEmpty()) {
            var totalAmount = 0.0
            val totalMember = 6
            for (rent in rentList) {
                totalAmount += rent.amount!!.toDouble()
            }
            perHeadCost = totalAmount / totalMember
            binding.totalAmountTv.text = decimalFormat.format(totalAmount).toString()
            binding.totalMemberTv.text = totalMember.toString()
            binding.costPerHeadTv.text = decimalFormat.format(perHeadCost).toString()
        }
        val accountId = session.getString("ACCOUNT_ID", "").toString()

        getSeparateRentList(accountId, perHeadCost)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onChangeSeparateRent(total1: String, total2: String, total3: String) {
        binding.total1Tv.text = total1
        binding.total2Tv.text = total2
        binding.total3Tv.text = total3
    }
}