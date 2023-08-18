package com.therishideveloper.bachelorpoint.ui.expense

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.therishideveloper.bachelorpoint.adapter.ExpenseAdapter
import com.therishideveloper.bachelorpoint.databinding.FragmentExpenseBinding
import com.therishideveloper.bachelorpoint.listener.ExpenseListener
import com.therishideveloper.bachelorpoint.listener.MyMonthAndYear
import com.therishideveloper.bachelorpoint.model.Expense
import com.therishideveloper.bachelorpoint.utils.MyCalender

class ExpenseFragment : Fragment(),ExpenseListener {

    private val TAG = "ExpenditureFragment"

    private var _binding: FragmentExpenseBinding? = null
    private val binding get() = _binding!!

    private lateinit var session: SharedPreferences
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExpenseBinding.inflate(inflater, container, false)
        session = requireContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        database = Firebase.database.reference.child("Bachelor Point").child("Users")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDatePicker()

    }

    private fun setupDatePicker() {
        binding.dateTv.text = MyCalender.currentMonthYear
        getExpenseList(MyCalender.currentMonthYear)
        binding.dateTv.setOnClickListener {
            MyCalender.pickMonthAndYear(activity, object : MyMonthAndYear {
                override fun onPickMonthAndYear(monthAndYear: String?) {
                    binding.dateTv.text = monthAndYear
                    if (monthAndYear != null) {
                        getExpenseList(monthAndYear)
                    }
                    Log.d(TAG, "monthAndYear: $monthAndYear")
                }

                override fun onPickDate(date: String?) {
                    Log.d(TAG, "date: $date")
                }
            })
        }
    }

    private fun getExpenseList(monthAndYear: String) {
        val listener = this
        val accountId = session.getString("ACCOUNT_ID", "").toString()
        Log.d(TAG, "onDataChange: accountId: $accountId")
        database.child(accountId).child("Expense")
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
                        val adapter = ExpenseAdapter(listener, dataList)
                        binding.recyclerView.adapter = adapter
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e(TAG, "DatabaseError", error.toException())
                    }
                }
            )
    }

    override fun onChangeExpense(expenseList: List<Expense>) {
        Log.d("TAG", "mealList.size: " + expenseList.size.toString())
        if (expenseList.isNotEmpty()) {
            var totalCost = 0
            for (expenditure in expenseList) {
                totalCost += expenditure.totalCost!!.toInt();
            }
            binding.totalAmountTv.text = totalCost.toString()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}