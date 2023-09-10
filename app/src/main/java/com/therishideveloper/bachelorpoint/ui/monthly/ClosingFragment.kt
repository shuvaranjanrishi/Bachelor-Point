package com.therishideveloper.bachelorpoint.ui.monthly

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.therishideveloper.bachelorpoint.R
import com.therishideveloper.bachelorpoint.adapter.ExpenseAdapter
import com.therishideveloper.bachelorpoint.adapter.ExpenseClosingAdapter
import com.therishideveloper.bachelorpoint.adapter.MealClosingAdapter
import com.therishideveloper.bachelorpoint.api.NetworkResult
import com.therishideveloper.bachelorpoint.databinding.FragmentClosingBinding
import com.therishideveloper.bachelorpoint.listener.ExpenseClosingListener
import com.therishideveloper.bachelorpoint.listener.MealClosingListener
import com.therishideveloper.bachelorpoint.listener.MyMonthAndYear
import com.therishideveloper.bachelorpoint.model.Expense
import com.therishideveloper.bachelorpoint.model.Meal
import com.therishideveloper.bachelorpoint.model.MealClosing
import com.therishideveloper.bachelorpoint.model.User
import com.therishideveloper.bachelorpoint.session.UserSession
import com.therishideveloper.bachelorpoint.ui.expense.ExpenseViewModel
import com.therishideveloper.bachelorpoint.ui.meal.MealViewModel
import com.therishideveloper.bachelorpoint.ui.member.MemberViewModel
import com.therishideveloper.bachelorpoint.utils.MyCalender
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ClosingFragment : Fragment(), MealClosingListener,ExpenseClosingListener {

    private val TAG = "ClosingFragment"

    private var _binding: FragmentClosingBinding? = null
    private val binding get() = _binding!!

    private val memberViewModel: MemberViewModel by viewModels()
    private val mealViewModel: MealViewModel by viewModels()
    private val expenseViewModel: ExpenseViewModel by viewModels()

    @Inject
    lateinit var session: UserSession
    private lateinit var database: DatabaseReference
    private var memberList: List<User> = mutableListOf()
    private lateinit var accountId: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClosingBinding.inflate(inflater, container, false)
        database =
            Firebase.database.reference.child(getString(R.string.database_name)).child("Accounts")
        accountId = session.getAccountId().toString()
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.monthTv.text = MyCalender.currentMonthYear

        getMembers()

        getMealListOfThisMonth(MyCalender.currentMonthYear)

        setupMonthPicker()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupMonthPicker() {
        binding.monthTv.setOnClickListener {
            MyCalender.pickMonthAndYear(activity, object : MyMonthAndYear {
                override fun onPickMonthAndYear(monthAndYear: String?) {
                    binding.monthTv.text = monthAndYear
                    if (monthAndYear != null) {
                        getMealListOfThisMonth(monthAndYear)
                        binding.monthTv.text = monthAndYear
                    }
                    Log.d(TAG, "monthAndYear: $monthAndYear")
                }

                override fun onPickDate(date: String?) {
                    Log.d(TAG, "date: $date")
                }
            })
        }
    }

    private fun getMembers() {
        memberViewModel.getMembers(accountId)

        memberViewModel.membersLiveData.observe(viewLifecycleOwner) {
            binding.mainLl.isVisible = false
            binding.progressBar.isVisible = false
            when (it) {
                is NetworkResult.Success -> {
                    this@ClosingFragment.memberList = it.data!!
                    binding.mainLl.isVisible = true
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

    private fun getMealListOfThisMonth(monthAndYear: String) {
        mealViewModel.getMealListOfAMonth(accountId, monthAndYear)

        mealViewModel.mealsLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.isVisible = false
            when (it) {
                is NetworkResult.Success -> {
                    val mealList = it.data!!
                    if (mealList.isNotEmpty()) {
                        expenseViewModel.getExpenses(monthAndYear,accountId,database)
                        getExpenseList(monthAndYear, mealList)
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

    private fun getExpenseList(monthAndYear: String, mealList: List<Meal>) {
        val listener = this
        expenseViewModel.expenseResponseLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.isVisible = false
            when (it) {
                is NetworkResult.Success -> {
                    sumIndividualClosingMeals(mealList, it.data!! as MutableList<Expense>)
                }

                is NetworkResult.Error -> {
                    Toast.makeText(context, "Error: ${it.message}", Toast.LENGTH_LONG).show()
                }

                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true
                }
            }
        }

    }

    private fun sumIndividualClosingMeals(mealList: List<Meal>, expenseList: MutableList<Expense>) {

        mealViewModel.sumIndividualClosingMeals(mealList, memberList, expenseList)

        val listener = this
        mealViewModel.sumMealsClosingLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.isVisible = false
            when (it) {
                is NetworkResult.Success -> {
                    val adapter = MealClosingAdapter(listener, it.data!!)
                    binding.recyclerView1.adapter = adapter
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

    override fun onChangeMeal(mealList: List<MealClosing>) {
        mealViewModel.totalExpenseClosingMeals(mealList)
        mealViewModel.totalExpenseLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.isVisible = false
            when (it) {
                is NetworkResult.Success -> {
                    val expenseList = it.data!!
                    getMealRate(expenseList)
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

    private fun getMealRate(expenseList: List<MealClosing>) {
        val listener = this
        mealViewModel.mealRateLiveData.observe(viewLifecycleOwner){
            binding.progressBar.isVisible = false
            when (it) {
                is NetworkResult.Success -> {
                    val data = it.data!!
                    binding.totalMealTv.text = data.split("-")[0]
                    binding.totalExpenseTv.text = data.split("-")[1]
                    binding.mealRateTv.text = data.split("-")[2]
                    val adapter = ExpenseClosingAdapter(listener, data.split("-")[2], expenseList)
                    binding.recyclerView2.adapter = adapter
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

    override fun onChangeExpense(totalCostOfMeal: String, totalResult: String) {
        binding.totalExpenseOfMealTv.text = totalCostOfMeal
        binding.totalResultTv.text = totalResult
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
//320