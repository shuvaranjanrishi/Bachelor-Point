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
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.therishideveloper.bachelorpoint.adapter.MealAdapter
import com.therishideveloper.bachelorpoint.databinding.FragmentMonthlyBinding
import com.therishideveloper.bachelorpoint.listener.MealListener
import com.therishideveloper.bachelorpoint.model.Meal
import com.therishideveloper.bachelorpoint.ui.meal.MealViewModel
import com.therishideveloper.bachelorpoint.utils.MyCalender

class MonthlyFragment : Fragment(),MealListener {

    private val TAG = "MealFragment"

    private var _binding: FragmentMonthlyBinding? = null
    private val binding get() = _binding!!

    private val memberViewModel: MealViewModel by viewModels()
    private lateinit var session: SharedPreferences
    private lateinit var database: DatabaseReference

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

        binding.monthTv.text = MyCalender.currentMonthYear
        binding.firstDateTv.text = MyCalender.firstDateOfMonth
        binding.lastDateTv.text = MyCalender.currentDate

        Log.d(
            TAG,
            "firstDateOfMonth: ${MyCalender.firstDateOfMonth} lastDateOfMonth ${MyCalender.lastDateOfMonth}"
        )

        getMealListOfThisMonth()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getMealListOfThisMonth() {

        val accountId = session.getString("ACCOUNT_ID", "").toString()
        val userId = session.getString("MEMBER_ID", "").toString()
        Log.d(TAG, "onDataChange: accountId: $accountId userId: $userId")

        val listener = this
        database.child(accountId).child("Meal").orderByChild("date")
            .startAt(MyCalender.firstDateOfMonth)
            .endAt(MyCalender.lastDateOfMonth)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val mealList: MutableList<Meal> = mutableListOf()
                        for (ds in dataSnapshot.children) {
                            val meal: Meal? = ds.getValue(Meal::class.java)
                            if (meal != null) {
                                val fMeal = meal.firstMeal!!.toInt() + meal.firstMeal!!.toInt()
                                val sMeal =
                                    meal.secondMeal!!.toInt() + meal.secondMeal!!.toInt()
                                val tMeal = meal.thirdMeal!!.toInt() + meal.thirdMeal!!.toInt()
                                val stMeal =
                                    meal.subTotalMeal!!.toInt() + meal.subTotalMeal!!.toInt()
                                mealList.add(
                                    Meal(
                                        meal.id,
                                        meal.memberId,
                                        meal.name,
                                        fMeal.toString(),
                                        sMeal.toString(),
                                        tMeal.toString(),
                                        stMeal.toString(),
                                        meal.date,
                                        meal.createdAt,
                                        meal.updatedAt
                                    )
                                )
                            }
                            if (mealList.contains(meal)) {
                                mealList.remove(meal)
                            }
                        }
                        Log.d(TAG, "onDataChange: mealList: $mealList")
                        val adapter = MealAdapter(listener, mealList)
                        binding.recyclerView.adapter = adapter
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e(TAG, "DatabaseError", error.toException())
                    }
                }
            )
    }

    override fun onChangeMeal(mealList: List<Meal>) {
        Log.d("TAG", "mealList.size: " + mealList.size.toString())
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