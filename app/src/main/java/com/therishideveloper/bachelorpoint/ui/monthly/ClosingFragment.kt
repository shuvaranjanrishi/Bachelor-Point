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
import com.therishideveloper.bachelorpoint.adapter.ExpenseClosingAdapter
import com.therishideveloper.bachelorpoint.adapter.MealClosingAdapter
import com.therishideveloper.bachelorpoint.databinding.FragmentClosingBinding
import com.therishideveloper.bachelorpoint.listener.ExpenseClosingListener
import com.therishideveloper.bachelorpoint.listener.MealClosingListener
import com.therishideveloper.bachelorpoint.model.Expense
import com.therishideveloper.bachelorpoint.model.ExpenseClosing
import com.therishideveloper.bachelorpoint.model.Meal
import com.therishideveloper.bachelorpoint.model.MealClosing
import com.therishideveloper.bachelorpoint.model.User
import com.therishideveloper.bachelorpoint.ui.meal.MealViewModel
import com.therishideveloper.bachelorpoint.ui.member.MemberViewModel
import com.therishideveloper.bachelorpoint.utils.MyCalender
import java.math.RoundingMode
import java.text.DecimalFormat


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
                        val dataList: MutableList<Expense> = mutableListOf()
                        for (ds in dataSnapshot.children) {
                            val expenditure: Expense? = ds.getValue(Expense::class.java)
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

    private fun sumIndividualMeals(mealList: MutableList<Meal>, expenseList: MutableList<Expense>) {

        val listener = this
        val newList: MutableList<MealClosing> = ArrayList()
        for (i in memberList.indices) {
            var id = ""
            var name = ""
            var createdAt = ""
            var date = ""
            var updatedAt = ""
            var subTotalMeal = 0
            var totalExpense = 0

            for (j in mealList.indices) {
                if (mealList[j].memberId.toString() == memberList.toTypedArray()[i].id) {
                    id = mealList[j].memberId.toString()
                    name = mealList[j].name.toString()
                    date = mealList[j].date.toString()
                    createdAt = mealList[j].createdAt.toString()
                    updatedAt = mealList[j].updatedAt.toString()
                    subTotalMeal += mealList[j].subTotalMeal!!.toInt()
                }
            }

            //sum expense
            if (expenseList.isNotEmpty()) {
                for (expenditure in expenseList) {
                    if (expenditure.memberId == memberList.toTypedArray()[i].id) {
                        totalExpense += expenditure.totalCost!!.toInt();
                    }
                }
            }
            newList.add(
                MealClosing(
                    "" + id,
                    "" + id,
                    "" + name,
                    "" + subTotalMeal,
                    "" + totalExpense,
                    "" + date,
                    "" + createdAt,
                    "" + updatedAt
                )
            )
        }

        val adapter = MealClosingAdapter(listener, newList.sortedBy { it.name })
        binding.recyclerView1.adapter = adapter
    }

    override fun onChangeMeal(mealList: List<MealClosing>) {
        val newList: MutableList<ExpenseClosing> = ArrayList()
        val listener = this

        try {
            if (mealList.isNotEmpty()) {
                var id = "0"
                var memberId = "0"
                var name = "0"
                var totalMeal = 0
                var totalExpense = 0
                for (meal in mealList) {
                    id = meal.id.toString()
                    memberId = meal.memberId.toString()
                    name = meal.name.toString()
                    totalMeal += meal.totalMeal!!.toInt()
                    totalExpense += meal.totalExpense!!.toInt()

                    newList.add(
                        ExpenseClosing(
                            "" + id,
                            "" + memberId,
                            "" + name,
                            "" + totalMeal,
                            "" + totalExpense,
                            "" + id,
                            "" + id
                        )
                    )
                }

                val mealRate = (totalExpense.toDouble() / totalMeal.toDouble())
                val df = DecimalFormat("#.##")
                df.roundingMode = RoundingMode.DOWN
                binding.totalMealTv.text = df.format(totalMeal).toString()
                binding.totalExpenseTv.text = df.format(totalExpense).toString()
                binding.mealRateTv.text = df.format(mealRate).toString()

                val adapter =
                    ExpenseClosingAdapter(listener, mealRate.toString(), mealList.sortedBy { it.name })
                binding.recyclerView2.adapter = adapter

            }
        } catch (e: Exception) {
            Log.e(TAG, "${e.message}")
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