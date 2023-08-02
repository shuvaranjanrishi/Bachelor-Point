package com.therishideveloper.bachelorpoint.ui.rent

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.therishideveloper.bachelorpoint.R
import com.therishideveloper.bachelorpoint.adapter.spinner.RentTypeSpAdapter
import com.therishideveloper.bachelorpoint.utils.MyCalender
import com.therishideveloper.bachelorpoint.databinding.FragmentAddRentBinding
import com.therishideveloper.bachelorpoint.listener.MealListener
import com.therishideveloper.bachelorpoint.model.Expenditure
import com.therishideveloper.bachelorpoint.model.Meal
import com.therishideveloper.bachelorpoint.model.Rent
import com.therishideveloper.bachelorpoint.model.User

class AddRentFragment : Fragment(), MealListener {

    private var _binding: FragmentAddRentBinding? = null
    private val binding get() = _binding!!

    private val rentViewModel: RentViewModel by viewModels()
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var session: SharedPreferences
    private lateinit var selectedUid: String
    private lateinit var selectedId: String
    private lateinit var selectedName: String
    private lateinit var amount: String
    private lateinit var description: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddRentBinding.inflate(inflater, container, false)

        auth = Firebase.auth
        database = Firebase.database.reference.child(getString(R.string.app_name)).child("Users")
        session = requireContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSpinner()

        binding.dateTv.text = (MyCalender.dayToday +" "+ MyCalender.currentDate)

        binding.amountEt.addTextChangedListener(
            onTextChanged = { s, _, _, _ ->
//               val inputs = binding.amountEt.text.toString().trim().isEmpty() ? "0" : binding.amountEt.text.toString()
//                if (inputs!=null){
//
//                }
            },
        )


        binding.saveBtn.setOnClickListener {

            amount = binding.amountEt.text.toString().trim()
            description = binding.descriptionEt.text.toString().trim()

            addRentAndBill(
                selectedId,
                selectedName,
                amount,
                description
            )

        }
    }

    private fun addRentAndBill(
        id: String,
        name: String,
        amount: String,
        description: String,
    ) {
        val accountId = session.getString("ACCOUNT_ID", "").toString()
        val timestamp = "" + System.currentTimeMillis()
        val rent =
            Rent(
                id,
                name,
                amount,
                description,
                timestamp,
                timestamp
            )
        database.child(accountId).child("RentAndBill").child(timestamp)
            .setValue(rent)
        Toast.makeText(
            context,
            "Rent And Bill Added Successful",
            Toast.LENGTH_SHORT,
        ).show()
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

    private fun setupSpinner() {
        rentViewModel.data.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                val adapter = RentTypeSpAdapter(requireContext(), it)
                binding.spinner.adapter = adapter

                binding.spinner.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(parent: AdapterView<*>?) {

                        }

                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            val selectedItem: Rent = parent?.getItemAtPosition(position) as Rent
                            selectedId = selectedItem.id.toString()
                            selectedName = selectedItem.name.toString()
                            Toast.makeText(
                                context,
                                "" + selectedItem.name + " Id: " + selectedItem.id,
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    }

            } else {
                Toast.makeText(context, "No Data Found", Toast.LENGTH_LONG).show()
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
