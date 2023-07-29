package com.therishideveloper.bachelorpoint.ui.rent

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.therishideveloper.bachelorpoint.utils.MyCalender
import com.therishideveloper.bachelorpoint.adapter.RentAdapter
import com.therishideveloper.bachelorpoint.databinding.FragmentRentBinding
import com.therishideveloper.bachelorpoint.listener.MyDate
import com.therishideveloper.bachelorpoint.listener.RentListener
import com.therishideveloper.bachelorpoint.model.Rent

class RentFragment : Fragment(), RentListener {

    private var _binding: FragmentRentBinding? = null
    private val binding get() = _binding!!

    private val memberViewModel: RentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.dateTv.setOnClickListener {
            MyCalender.pickDate(activity, object : MyDate {
                override fun onPickDate(date: String?) {
                    binding.dateTv.text = date
                }
            })
        }

        val listener = this
        memberViewModel.data.observe(viewLifecycleOwner) {
            val adapter = RentAdapter(listener, it)
            binding.recyclerView.adapter = adapter
        }
    }

    override fun onChangeRent(rentList: List<Rent>) {
        Log.d("TAG", "mealList.size: " + rentList.size.toString())
        if (rentList.isNotEmpty()) {
            var totalAmount = 0
            val totalMember = 6
            var costPerHead = 0
            for (meal in rentList) {
                totalAmount += meal.amount.toInt();
                binding.totalAmountTv.text = totalAmount.toString()
            }
            costPerHead = totalAmount / totalMember;
            binding.costPerHeadTv.text = costPerHead.toString()
            binding.totalMemberTv.text = totalMember.toString()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}