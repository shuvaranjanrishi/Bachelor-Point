package com.therishideveloper.bachelorpoint.ui.rent

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.firebase.database.DatabaseReference
import com.therishideveloper.bachelorpoint.adapter.RentAdapter
import com.therishideveloper.bachelorpoint.adapter.SeparateRentAdapter
import com.therishideveloper.bachelorpoint.api.NetworkResult
import com.therishideveloper.bachelorpoint.databinding.FragmentRentBinding
import com.therishideveloper.bachelorpoint.listener.MyMonthAndYear
import com.therishideveloper.bachelorpoint.listener.RentListener
import com.therishideveloper.bachelorpoint.listener.SeparateRentListener
import com.therishideveloper.bachelorpoint.model.Rent
import com.therishideveloper.bachelorpoint.reference.DBRef
import com.therishideveloper.bachelorpoint.session.UserSession
import com.therishideveloper.bachelorpoint.utils.MyCalender
import dagger.hilt.android.AndroidEntryPoint
import java.math.RoundingMode
import java.text.DecimalFormat
import javax.inject.Inject

@AndroidEntryPoint
class RentFragment : Fragment(), RentListener, SeparateRentListener {

    private val TAG = "RentFragment"

    private var _binding: FragmentRentBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var session: UserSession

    @Inject
    lateinit var dbRef: DBRef
    private lateinit var database: DatabaseReference
    private lateinit var accountId: String
    private lateinit var monthAndYear: String
    private lateinit var decimalFormat: DecimalFormat
    private val rentViewModel: RentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRentBinding.inflate(inflater, container, false)
        database = dbRef.getAccountRef()
        accountId = session.getAccountId().toString()
        decimalFormat = DecimalFormat("#.##")
        decimalFormat.roundingMode = RoundingMode.UP
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getRentAndBill(MyCalender.currentMonthYear)

        setupDatePiker()
    }

    private fun setupDatePiker() {
        binding.dateTv.text = MyCalender.currentMonthYear
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

    private fun getRentAndBill(monthAndYear: String) {

        rentViewModel.getRentAndBill(accountId, monthAndYear, database)
        val listener = this
        rentViewModel.rentLiveData.observe(viewLifecycleOwner) {
            binding.progressBar1.isVisible = false
            when (it) {
                is NetworkResult.Success -> {
                    val adapter = RentAdapter(listener, it.data!!)
                    binding.recyclerView1.adapter = adapter
                }

                is NetworkResult.Error -> {
                    Log.e(TAG, "Error: ${it.message}")
                }

                is NetworkResult.Loading -> {
                    binding.progressBar1.isVisible = true
                }
            }
        }
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
        getSeparateRentList(accountId, perHeadCost)
    }

    private fun getSeparateRentList(accountId: String, perHeadCost: Double) {
        rentViewModel.getSeparateRentList(accountId, monthAndYear, database)
        val listener = this
        rentViewModel.separateRentLiveData.observe(viewLifecycleOwner) {
            binding.progressBar2.isVisible = false
            when (it) {
                is NetworkResult.Success -> {
                    val separateRentList = it.data!!
                    if (separateRentList.isNotEmpty()) {
                        val adapter = SeparateRentAdapter(listener, perHeadCost, separateRentList)
                        binding.recyclerView2.adapter = adapter
                    }
                }

                is NetworkResult.Error -> {
                    Log.e(TAG, "Error: ${it.message}")
                }

                is NetworkResult.Loading -> {
                    binding.progressBar2.isVisible = true
                }
            }
        }
    }

    override fun onChangeSeparateRent(total1: String, total2: String, total3: String) {
        binding.total1Tv.text = total1
        binding.total2Tv.text = total2
        binding.total3Tv.text = total3
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
//209