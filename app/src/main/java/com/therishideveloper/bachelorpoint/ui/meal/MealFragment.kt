package com.therishideveloper.bachelorpoint.ui.meal

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.therishideveloper.bachelorpoint.R
import com.therishideveloper.bachelorpoint.adapter.MealAdapter
import com.therishideveloper.bachelorpoint.api.NetworkResult
import com.therishideveloper.bachelorpoint.databinding.FragmentMealBinding
import com.therishideveloper.bachelorpoint.listener.MealListener
import com.therishideveloper.bachelorpoint.listener.MyDayMonthYear
import com.therishideveloper.bachelorpoint.listener.MyMonthAndYear
import com.therishideveloper.bachelorpoint.model.Meal
import com.therishideveloper.bachelorpoint.model.User
import com.therishideveloper.bachelorpoint.ui.member.MemberViewModel
import com.therishideveloper.bachelorpoint.utils.MyCalender
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MealFragment : Fragment(), MealListener {

    private val TAG = "MealFragment"

    private var _binding: FragmentMealBinding? = null
    private val binding get() = _binding!!

    private lateinit var session: SharedPreferences
    private lateinit var database: DatabaseReference
    private lateinit var dayName: String
    private lateinit var monthAndYear: String
    private lateinit var date: String
    private lateinit var accountId: String
    private val memberViewModel: MemberViewModel by viewModels()
    private val mealViewModel: MealViewModel by viewModels()
    private var memberList: List<User> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMealBinding.inflate(inflater, container, false)
        database =
            Firebase.database.reference.child(getString(R.string.database_name)).child("Accounts")
        session = requireContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        accountId = session.getString("ACCOUNT_ID", "").toString()
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getMembers()

        setupDatePicker()

        setupMonthPicker()
    }

    private fun getMembers() {
        memberViewModel.getMembers(accountId)
        memberViewModel.membersLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.isVisible = false
            when (it) {
                is NetworkResult.Success -> {
                    this@MealFragment.memberList = it.data!!
                }

                is NetworkResult.Error -> {
                    Log.e(TAG, "Error: ${it.message}")
                }

                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true
                }
            }
        }
    }

    private fun setupDatePicker() {
        binding.monthTv.text = MyCalender.currentMonthYear
        binding.dateTv.text = MyCalender.currentDayAndDate
        getMealListOfADay(MyCalender.currentMonthYear, MyCalender.currentDate)
        binding.dateTv.setOnClickListener {
            MyCalender.pickDayMonthYear(activity, object : MyDayMonthYear {
                override fun onPickDayMonthYear(
                    dayName: String?,
                    monthYear: String?,
                    date: String?
                ) {
                    if (dayName != null) {
                        this@MealFragment.dayName = dayName
                        if (monthYear != null) {
                            this@MealFragment.monthAndYear = monthYear
                            binding.monthTv.text = monthYear
                        }
                        if (date != null) {
                            this@MealFragment.date = date
                            getMealListOfADay(monthAndYear, date)
                        }
                        binding.dateTv.text = dayName + " " + date
                    }
                }
            })
        }
    }

    private fun getMealListOfADay(monthAndYear: String, date: String) {
        val listener = this

        mealViewModel.getMealListOfADay(accountId, monthAndYear, date)

        mealViewModel.mealsLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.isVisible = false
            when (it) {
                is NetworkResult.Success -> {
                    val adapter = MealAdapter(listener, it.data!!)
                    binding.recyclerView.adapter = adapter
                }

                is NetworkResult.Error -> {
                    Log.e(TAG, "Error: ${it.message}")
                }

                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true
                }
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupMonthPicker() {
        binding.monthTv.setOnClickListener {
            MyCalender.pickMonthAndYear(activity, object : MyMonthAndYear {
                override fun onPickMonthAndYear(monthAndYear: String?) {
                    binding.monthTv.text = monthAndYear
                    if (monthAndYear != null) {
                        getMealListOfThisMonth(monthAndYear)
                        binding.dateTv.text = getString(R.string.day_dd_mm_yyyy)
                    }
                    Log.d(TAG, "monthAndYear: $monthAndYear")
                }

                override fun onPickDate(date: String?) {
                    Log.d(TAG, "date: $date")
                }
            })
        }
    }

    private fun getMealListOfThisMonth(monthAndYear: String) {
        val listener = this
        mealViewModel.getMealListOfAMonth(accountId, monthAndYear)

        mealViewModel.monthlyMealsLiveData.observe(viewLifecycleOwner) { it ->
            binding.progressBar.isVisible = false
            when (it) {
                is NetworkResult.Success -> {
                    val mealList = it.data!!
                    if (mealList.isNotEmpty()) {
                        sumIndividualMeals(mealList)
                    } else {
                        val adapter = MealAdapter(listener, mealList.sortedBy { it.name })
                        binding.recyclerView.adapter = adapter
                    }
                }

                is NetworkResult.Error -> {
                    Log.e(TAG, "Error: ${it.message}")
                }

                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true
                }
            }
        }
    }

    private fun sumIndividualMeals(mealList: List<Meal>) {

        mealViewModel.sumIndividualMeals(memberList, mealList)

        val listener = this
        mealViewModel.sumMealsLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.isVisible = false
            when (it) {
                is NetworkResult.Success -> {
                    val adapter = MealAdapter(listener, it.data!!)
                    binding.recyclerView.adapter = adapter
                }

                is NetworkResult.Error -> {
                    Log.e(TAG, "Error: ${it.message}")
                }

                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true
                }
            }
        }
    }

    override fun onChangeMeal(mealList: List<Meal>) {
        mealViewModel.totalMeals(mealList)
        mealViewModel.totalMealsLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.isVisible = false
            when (it) {
                is NetworkResult.Success -> {
                    val meal = it.data!!
                    binding.totalFirstMealTv.text = meal.firstMeal.toString()
                    binding.totalSecondMealTv.text = meal.secondMeal.toString()
                    binding.totalThirdMealTv.text = meal.thirdMeal.toString()
                    binding.totalMealTv.text = meal.subTotalMeal.toString()
                }

                is NetworkResult.Error -> {
                    Log.e(TAG, "Error: ${it.message}")
                }

                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
//247