package com.therishideveloper.bachelorpoint.ui.expense

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.therishideveloper.bachelorpoint.api.NetworkResult
import com.therishideveloper.bachelorpoint.databinding.FragmentExpenseBinding
import com.therishideveloper.bachelorpoint.listener.ExpenseListener
import com.therishideveloper.bachelorpoint.listener.MyMonthAndYear
import com.therishideveloper.bachelorpoint.model.Expense
import com.therishideveloper.bachelorpoint.session.UserSession
import com.therishideveloper.bachelorpoint.utils.MyCalender
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ExpenseFragment : Fragment(),ExpenseListener {

    private val TAG = "ExpenditureFragment"

    private var _binding: FragmentExpenseBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var session: UserSession
    private lateinit var database: DatabaseReference
    private lateinit var accountId: String

    private val expenseViewModel: ExpenseViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExpenseBinding.inflate(inflater, container, false)
        database = Firebase.database.reference.child(getString(R.string.database_name)).child("Accounts")
        accountId = session.getAccountId().toString()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDatePicker()

        getExpenseResponse()
    }

    private fun getExpenseResponse() {
        val listener = this
        expenseViewModel.expenseResponseLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.isVisible = false
            when (it) {
                is NetworkResult.Success -> {
                    val adapter = ExpenseAdapter(listener, it.data!!)
                    binding.recyclerView.adapter = adapter
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

    private fun setupDatePicker() {
        expenseViewModel.getExpenses(MyCalender.currentMonthYear, accountId, database)
        binding.dateTv.setOnClickListener {
            MyCalender.pickMonthAndYear(activity, object : MyMonthAndYear {
                override fun onPickMonthAndYear(monthAndYear: String?) {
                    binding.dateTv.text = monthAndYear
                    if (monthAndYear != null) {
                        expenseViewModel.getExpenses(monthAndYear, accountId, database)
                    }
                    Log.d(TAG, "monthAndYear: $monthAndYear")
                }

                override fun onPickDate(date: String?) {
                    Log.d(TAG, "date: $date")
                }
            })
        }
    }

    override fun onChangeExpense(expenseList: List<Expense>) {
        if (expenseList.isNotEmpty()) {
            var totalCost = 0
            for (expenditure in expenseList) {
                totalCost += expenditure.totalCost!!.toInt()
            }
            binding.totalAmountTv.text = totalCost.toString()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}