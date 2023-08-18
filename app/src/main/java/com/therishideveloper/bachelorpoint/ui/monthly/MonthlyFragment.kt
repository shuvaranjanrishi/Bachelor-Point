package com.therishideveloper.bachelorpoint.ui.monthly

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.childEvents
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.therishideveloper.bachelorpoint.adapter.MealAdapter
import com.therishideveloper.bachelorpoint.databinding.FragmentMonthlyBinding
import com.therishideveloper.bachelorpoint.listener.MealListener
import com.therishideveloper.bachelorpoint.listener.MyMonthAndYear
import com.therishideveloper.bachelorpoint.model.Meal
import com.therishideveloper.bachelorpoint.model.User
import com.therishideveloper.bachelorpoint.ui.meal.MealViewModel
import com.therishideveloper.bachelorpoint.ui.member.MemberViewModel
import com.therishideveloper.bachelorpoint.utils.MyCalender


class MonthlyFragment : Fragment(),MealListener {

    private val TAG = "MonthlyFragment"

    private var _binding: FragmentMonthlyBinding? = null
    private val binding get() = _binding!!

    private val memberViewModel: MemberViewModel by viewModels()
    private lateinit var session: SharedPreferences
    private lateinit var database: DatabaseReference
    private var memberList: List<User> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMonthlyBinding.inflate(inflater, container, false)
        session = requireContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        database = Firebase.database.reference.child("Bachelor Point").child("Users")
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        memberViewModel.data.observe(viewLifecycleOwner) {
            memberList = it
        }

        setupDatePicker()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupDatePicker() {
        binding.monthTv.text = MyCalender.currentMonthYear
        binding.firstDateTv.text = MyCalender.firstDateOfMonth
        binding.lastDateTv.text = MyCalender.currentDate
        getMealListOfThisMonth(MyCalender.currentMonthYear)
        binding.monthTv.setOnClickListener {
            MyCalender.pickMonthAndYear(activity, object : MyMonthAndYear {
                override fun onPickMonthAndYear(monthAndYear: String?) {
                    binding.monthTv.text = monthAndYear
                    if (monthAndYear != null) {
                        getMealListOfThisMonth(monthAndYear)
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
        val mealList: MutableList<Meal> = mutableListOf()
        val accountId = session.getString("ACCOUNT_ID", "").toString()
        val memberId = session.getString("ID", "").toString()

        database.child(accountId).child("Meal")
            .child(monthAndYear)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (dateValue in dataSnapshot.children) {
                            for (mealValue in dateValue.children) {
                                val meal: Meal? = mealValue.getValue(Meal::class.java)
                                mealList.add(meal!!)
                            }
                        }
                        sumIndividualMeals(mealList)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e(TAG, "DatabaseError", error.toException())
                    }
                }
            )
    }

    private fun sumIndividualMeals(mealList: MutableList<Meal>) {

        val listener = this
        val newList: MutableList<Meal> = ArrayList()
        for (i in memberList.indices) {
            var id = ""
            var name = ""
            var createdAt = ""
            var date = ""
            var updatedAt = ""
            var firstMeal = 0
            var secondMeal = 0
            var thirdMeal = 0
            var subTotalMeal = 0
            for (j in mealList.indices) {
                if (mealList[j].memberId.toString() == memberList.toTypedArray()[i].id) {
                    id = mealList[j].memberId.toString()
                    name = mealList[j].name.toString()
                    date = mealList[j].date.toString()
                    createdAt = mealList[j].createdAt.toString()
                    updatedAt = mealList[j].updatedAt.toString()
                    firstMeal += mealList[j].firstMeal!!.toInt()
                    secondMeal += mealList[j].secondMeal!!.toInt()
                    thirdMeal += mealList[j].thirdMeal!!.toInt()
                    subTotalMeal += mealList[j].subTotalMeal!!.toInt()
                }
            }
            newList.add(
                Meal(
                    "" + id,
                    "" + id,
                    "" + name,
                    "" + firstMeal,
                    "" + secondMeal,
                    "" + thirdMeal,
                    "" + subTotalMeal,
                    "" + date,
                    ""+createdAt,
                    ""+updatedAt
                )
            )
        }

        val adapter = MealAdapter(listener, newList.sortedBy { it.name })
        binding.recyclerView.adapter = adapter
    }

    override fun onChangeMeal(mealList: List<Meal>) {
        if (mealList.isNotEmpty()) {
            var totalMeal = 0
            var totalFirstMeal = 0
            var totalSecondMeal = 0
            var totalThirdMeal = 0
            for (meal in mealList) {
                totalFirstMeal += meal.firstMeal!!.toInt();
                totalSecondMeal += meal.secondMeal!!.toInt();
                totalThirdMeal += meal.thirdMeal!!.toInt();
                totalMeal += meal.subTotalMeal!!.toInt();

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