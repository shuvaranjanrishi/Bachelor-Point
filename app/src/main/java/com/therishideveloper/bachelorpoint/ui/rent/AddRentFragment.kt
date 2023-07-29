package com.therishideveloper.bachelorpoint.ui.rent

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.therishideveloper.bachelorpoint.utils.MyCalender
import com.therishideveloper.bachelorpoint.databinding.FragmentAddRentBinding
import com.therishideveloper.bachelorpoint.listener.MealListener
import com.therishideveloper.bachelorpoint.model.Meal

class AddRentFragment : Fragment(), MealListener {

    private var _binding: FragmentAddRentBinding? = null
    private val binding get() = _binding!!

    private val memberViewModel: RentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddRentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.dateTv.text = (MyCalender.dayToday +" "+ MyCalender.currentDate)

        binding.amountEt.addTextChangedListener(
            onTextChanged = { s, _, _, _ ->
//               val inputs = binding.amountEt.text.toString().trim().isEmpty() ? "0" : binding.amountEt.text.toString()
//                if (inputs!=null){
//
//                }
            },
        )

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
//                val adapter = AddRentAdapter(listener, it)
//                binding.recyclerView.adapter = adapter
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

//                binding.totalFirstMealTv.text = totalFirstMeal.toString()
//                binding.totalSecondMealTv.text = totalSecondMeal.toString()
//                binding.totalThirdMealTv.text = totalThirdMeal.toString()
//                binding.totalMealTv.text = totalMeal.toString()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
