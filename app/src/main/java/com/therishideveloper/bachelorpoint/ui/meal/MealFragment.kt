package com.therishideveloper.bachelorpoint.ui.meal

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TableRow.LayoutParams
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.therishideveloper.bachelorpoint.R
import com.therishideveloper.bachelorpoint.adapter.MealAdapter
import com.therishideveloper.bachelorpoint.api.NetworkResult
import com.therishideveloper.bachelorpoint.databinding.FragmentMealBinding
import com.therishideveloper.bachelorpoint.listener.MealListener
import com.therishideveloper.bachelorpoint.listener.MyDayMonthYear
import com.therishideveloper.bachelorpoint.listener.MyMonthAndYear
import com.therishideveloper.bachelorpoint.model.Meal
import com.therishideveloper.bachelorpoint.model.User
import com.therishideveloper.bachelorpoint.session.UserSession
import com.therishideveloper.bachelorpoint.ui.member.MemberViewModel
import com.therishideveloper.bachelorpoint.utils.MyCalender
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.math.roundToInt


@AndroidEntryPoint
class MealFragment : Fragment(), MealListener {

    private val TAG = "MealFragment"

    private var _binding: FragmentMealBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var session: UserSession
    private lateinit var dayName: String
    private lateinit var monthAndYear: String
    private lateinit var date: String
    private lateinit var accountId: String
    private val mealViewModel: MealViewModel by viewModels()
    private val memberViewModel: MemberViewModel by viewModels()
    lateinit var table: TableLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMealBinding.inflate(inflater, container, false)
        accountId = session.getAccountId().toString()
        table = binding.tableLayout
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDatePicker()

        setupMonthPicker()
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
//                        getMealListOfAMonth(monthAndYear)
                        getMonthView(monthAndYear)
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

    private fun getMonthView(monthAndYear: String) {
        mealViewModel.getMonthView(accountId, monthAndYear)
        mealViewModel.monthView.observe(viewLifecycleOwner) {
            binding.progressBar.isVisible = false
            when (it) {
                is NetworkResult.Success -> {
                    setupMonthView(it.data!!)
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

    private fun getMealListOfAMonth(monthAndYear: String) {
        mealViewModel.getMealListOfAMonth(accountId, monthAndYear)
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

    private fun setupMonthView(data: List<Meal>) {
        var memberList: List<User>
        memberViewModel.getMembers(accountId)
        memberViewModel.membersLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.isVisible = false
            when (it) {
                is NetworkResult.Success -> {
                    memberList = it.data!!
                    getListData(memberList, data)
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

    private fun getListData(memberList: List<User>, data: List<Meal>) {
        Log.d(TAG, "data: $data")
        Log.d(TAG, "datasize: ${data.size}")
        val layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        table.isStretchAllColumns = true
        var i: Int = 0
        table.bringToFront()

        val row1 = TableRow(context)
        for (d in 1..30) {
            val date = TextView(context)
            date.textSize = 18f
            date.text = date.toString()
            date.layoutParams = layoutParams
            row1.addView(date, i)
            table.addView(row1,0)

        }
        for (rowIndex in memberList.indices + 1) {
            val row = TableRow(context)
//            val row1 = TableRow(context)

            for (col in data.indices) {
                if (memberList[rowIndex].id == data[col].memberId) {
//                    val date = TextView(context)
//                    date.textSize = 18f
//                    date.text = data[col].date
//                    date.layoutParams = layoutParams
//                    row.addView(date, i)

                    val meal = TextView(context)
                    meal.textSize = 18f
                    meal.text = data[col].subTotalMeal!!.toDouble().roundToInt().toString() + " "
                    meal.layoutParams = layoutParams
                    row.addView(meal, i)
                    i++
                }
            }
            val name = TextView(context)
            name.textSize = 18f
            name.text = memberList[rowIndex].name + " "
            name.layoutParams = layoutParams
            row.addView(name, 0)
//            val date = TextView(context)
//            date.textSize = 18f
//            date.text = "Date   "
//            date.layoutParams = layoutParams
//            row1.addView(date, 0)
//            table.addView(row1,0)
            i = 0
            table.addView(row, rowIndex)
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