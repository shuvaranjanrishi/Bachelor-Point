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
import com.therishideveloper.bachelorpoint.adapter.ExpenditureAdapter
import com.therishideveloper.bachelorpoint.adapter.MealAdapter
import com.therishideveloper.bachelorpoint.adapter.MealClosingAdapter
import com.therishideveloper.bachelorpoint.databinding.FragmentClosingBinding
import com.therishideveloper.bachelorpoint.databinding.FragmentMonthlyBinding
import com.therishideveloper.bachelorpoint.listener.ExpenseClosingListener
import com.therishideveloper.bachelorpoint.listener.MealClosingListener
import com.therishideveloper.bachelorpoint.listener.MealListener
import com.therishideveloper.bachelorpoint.model.Expenditure
import com.therishideveloper.bachelorpoint.model.ExpenseClosing
import com.therishideveloper.bachelorpoint.model.Meal
import com.therishideveloper.bachelorpoint.model.MealClosing
import com.therishideveloper.bachelorpoint.model.User
import com.therishideveloper.bachelorpoint.ui.meal.MealViewModel
import com.therishideveloper.bachelorpoint.ui.member.MemberViewModel
import com.therishideveloper.bachelorpoint.utils.MyCalender


class ClosingFragment : Fragment(), MealClosingListener,ExpenseClosingListener {

    private val TAG = "ClosingFragment"

    private var _binding: FragmentClosingBinding? = null
    private val binding get() = _binding!!

    private val memberViewModel: MemberViewModel by viewModels()
    private val mealViewModel: MealViewModel by viewModels()
    private lateinit var session: SharedPreferences
    private lateinit var database: DatabaseReference
    private var memberList: List<User> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClosingBinding.inflate(inflater, container, false)
        session = requireContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        database = Firebase.database.reference.child("Bachelor Point").child("Users")

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.monthTv.text = MyCalender.currentMonthYear

        memberViewModel.data.observe(viewLifecycleOwner) {
            memberList = it
        }

        getMealListOfThisMonth(MyCalender.currentMonthYear)

    }

    private fun getExpenditures(monthAndYear: String,mealList: MutableList<Meal>) {
        val accountId = session.getString("ACCOUNT_ID", "").toString()
        Log.d(TAG, "onDataChange: accountId: $accountId")
        database.child(accountId).child("Expenditure")
            .orderByChild("monthAndYear")
            .equalTo(monthAndYear)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val dataList: MutableList<Expenditure> = mutableListOf()
                        for (ds in dataSnapshot.children) {
                            val expenditure: Expenditure? = ds.getValue(Expenditure::class.java)
                            dataList.add(expenditure!!)
                        }
                        sumIndividualMeals(mealList,dataList)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e(TAG, "DatabaseError", error.toException())
                    }
                }
            )
    }

    private fun getMealListOfThisMonth(monthAndYear: String) {
        val mealList: MutableList<Meal> = mutableListOf()
        val accountId = session.getString("ACCOUNT_ID", "").toString()

        database.child(accountId).child("Meal")
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (dateValue in dataSnapshot.children) {
                            for (mealValue in dateValue.children) {
                                val meal: Meal? = mealValue.getValue(Meal::class.java)
                                mealList.add(meal!!)
                            }
                        }
                        getExpenditures(monthAndYear,mealList)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e(TAG, "DatabaseError", error.toException())
                    }
                }
            )
    }

    private fun sumIndividualMeals(mealList: MutableList<Meal>, expenditureList: MutableList<Expenditure>) {

        val listener = this
        val newList: MutableList<MealClosing> = ArrayList()
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

            //sum expense
            var totalExpenditure = 0
            if (expenditureList.isNotEmpty()) {
                for (expenditure in expenditureList) {
                    if (expenditure.memberId == memberList.toTypedArray()[i].id) {
                        totalExpenditure += expenditure.totalCost!!.toInt();
                    }
                }
            }
            newList.add(
                MealClosing(
                    "" + id,
                    "" + id,
                    "" + name,
                    "" + firstMeal,
                    "" + secondMeal,
                    "" + thirdMeal,
                    "" + subTotalMeal,
                    ""+totalExpenditure,
                    "" + date,
                    ""+createdAt,
                    ""+updatedAt
                )
            )
        }

        val adapter = MealClosingAdapter(listener, newList.sortedBy { it.name })
        binding.recyclerView1.adapter = adapter
    }

    override fun onChangeMeal(mealList: List<MealClosing>) {
        if (mealList.isNotEmpty()) {
            var totalMeal = 0
            var totalFirstMeal = 0
            var totalSecondMeal = 0
            var totalExpenditure = 0
            for (meal in mealList) {
                totalFirstMeal += meal.firstMeal!!.toInt();
                totalSecondMeal += meal.secondMeal!!.toInt();
                totalMeal += meal.subTotalMeal!!.toInt();
                totalExpenditure += meal.totalExpenditure!!.toInt();

                binding.totalMealTv.text = totalMeal.toString()
                binding.totalExpenseTv.text = totalExpenditure.toString()
            }
        }
    }
    override fun onChangeExpense(mealList: List<ExpenseClosing>) {
        if (mealList.isNotEmpty()) {
            var totalMeal = 0
            var totalExpenditure = 0
            for (meal in mealList) {
                totalMeal += meal.totalMeal!!.toInt();
                totalExpenditure += meal.totalExpense!!.toInt();

                binding.totalMealTv.text = totalMeal.toString()
                binding.totalExpenseTv.text = totalExpenditure.toString()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}