package com.therishideveloper.bachelorpoint.ui.expenditure

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.therishideveloper.bachelorpoint.adapter.ExpenditureAdapter
import com.therishideveloper.bachelorpoint.adapter.MealAdapter
import com.therishideveloper.bachelorpoint.databinding.FragmentExpenditureBinding
import com.therishideveloper.bachelorpoint.listener.MealListener
import com.therishideveloper.bachelorpoint.databinding.FragmentMealBinding
import com.therishideveloper.bachelorpoint.listener.ExpenditureListener
import com.therishideveloper.bachelorpoint.model.Expenditure
import com.therishideveloper.bachelorpoint.model.Meal

class ExpenditureFragment : Fragment(),ExpenditureListener {

    private var _binding: FragmentExpenditureBinding? = null
    private val binding get() = _binding!!

    private val memberViewModel: ExpenditureViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExpenditureBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listener = this
        memberViewModel.data.observe(viewLifecycleOwner) {
            val adapter = ExpenditureAdapter(listener,it)
            binding.recyclerView.adapter = adapter
        }
    }

    override fun onChangeExpenditure(expenditureList: List<Expenditure>) {
        Log.d("TAG", "mealList.size: " + expenditureList.size.toString())
        if (expenditureList.isNotEmpty()) {
            var totalCost = 0
            for (expenditure in expenditureList) {
                totalCost += expenditure.totalCost!!.toInt();
            }
            binding.totalAmountTv.text = totalCost.toString()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}