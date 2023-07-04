package com.therishideveloper.bachelorpoint.ui.meal

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.therishideveloper.bachelorpoint.adapter.MealAdapter
import com.therishideveloper.bachelorpoint.listener.MealListener
import com.therishideveloper.bachelorpoint.databinding.FragmentMealBinding
import com.therishideveloper.bachelorpoint.model.Meal

class MealFragment : Fragment(),MealListener {

    private var _binding: FragmentMealBinding? = null
    private val binding get() = _binding!!

    private val memberViewModel: MealViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMealBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listener = this
        memberViewModel.data.observe(viewLifecycleOwner) {
            val adapter = MealAdapter(listener,it)
            binding.recyclerView.adapter = adapter
        }
    }

    override fun onChangeMeal(mealList: List<Meal>) {
        Log.d("TAG", "mealList.size: " + mealList.size.toString())
        if (mealList.isNotEmpty()) {
            var totalMeal = 0
            var totalFirstMeal = 0
            var totalSecondMeal = 0
            var totalThirdMeal = 0
            for (meal in mealList) {
                totalFirstMeal += meal.firstMeal.toInt();
                totalSecondMeal += meal.secondMeal.toInt();
                totalThirdMeal += meal.thirdMeal.toInt();
                totalMeal += meal.subTotalMeal.toInt();

                binding.totalFirstMealTv.text = totalFirstMeal.toString()
                binding.totalSecondMealTv.text = totalSecondMeal.toString()
                binding.totalThirdMealTv.text = totalThirdMeal.toString()
                binding.totalMealTv.text = totalMeal.toString()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}