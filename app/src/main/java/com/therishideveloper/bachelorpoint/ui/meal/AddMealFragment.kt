package com.therishideveloper.bachelorpoint.ui.meal

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.therishideveloper.bachelorpoint.adapter.AddMealAdapter
import com.therishideveloper.bachelorpoint.databinding.FragmentAddMealBinding
import com.therishideveloper.bachelorpoint.listener.MealListener
import com.therishideveloper.bachelorpoint.model.Meal

class AddMealFragment : Fragment(), MealListener {

    private var _binding: FragmentAddMealBinding? = null
    private val binding get() = _binding!!

    private val memberViewModel: MealViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddMealBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val name: String = binding.nameEt.text.toString().trim()
//        val email: String = binding.emailEt.text.toString().trim()
//        val address: String = binding.addressEt.text.toString().trim()
//        val phone: String = binding.phoneEt.text.toString().trim()
//
//        binding.saveBtn.setOnClickListener {
//            val timestamp: String = "" + System.currentTimeMillis();
//            val member = Member("1", name, email, address, phone, timestamp, timestamp)
//            Log.d("AddMember", "Member: $member")
//        }
        val listener = this
        memberViewModel.data.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                val adapter = AddMealAdapter(listener, it)
                binding.recyclerView.adapter = adapter
//                binding.progressBar.isVisible = false
//                binding.mainLl.isVisible = true
            } else {
                Toast.makeText(context, "No Data Found", Toast.LENGTH_LONG).show()
            }

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