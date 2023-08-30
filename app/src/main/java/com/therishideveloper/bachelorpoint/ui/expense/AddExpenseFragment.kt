package com.therishideveloper.bachelorpoint.ui.expense

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.therishideveloper.bachelorpoint.R
import com.therishideveloper.bachelorpoint.adapter.MemberAdapter
import com.therishideveloper.bachelorpoint.adapter.spinner.MemberSpAdapter
import com.therishideveloper.bachelorpoint.api.NetworkResult
import com.therishideveloper.bachelorpoint.databinding.FragmentAddExpenseBinding
import com.therishideveloper.bachelorpoint.listener.MyDayMonthYear
import com.therishideveloper.bachelorpoint.model.Expense
import com.therishideveloper.bachelorpoint.model.User
import com.therishideveloper.bachelorpoint.ui.member.MemberViewModel
import com.therishideveloper.bachelorpoint.utils.MyCalender
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddExpenseFragment : Fragment() {

    private val TAG = "AddExpenditureFragment"

    private var _binding: FragmentAddExpenseBinding? = null
    private val binding get() = _binding!!

    private val memberViewModel: MemberViewModel by viewModels()
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var session: SharedPreferences
    private lateinit var selectedUid: String
    private lateinit var selectedId: String
    private lateinit var selectedName: String
    private lateinit var amount: String
    private lateinit var description: String
    private lateinit var dayName: String
    private lateinit var monthAndYear: String
    private lateinit var date: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddExpenseBinding.inflate(inflater, container, false)

        auth = Firebase.auth
        database = Firebase.database.reference.child(getString(R.string.database_name)).child("Accounts")
        session = requireContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDatePicker()

        getMembers()

        binding.saveBtn.setOnClickListener {

            amount = binding.amountEt.text.toString().trim()
            description = binding.descriptionEt.text.toString().trim()

            addExpense(
                amount,
                description
            )

        }

    }

    private fun setupDatePicker() {
        binding.dateTv.text = MyCalender.currentDayAndDate
        monthAndYear = MyCalender.currentMonthYear
        date = MyCalender.currentDate
        binding.dateTv.setOnClickListener {
            MyCalender.pickDayMonthYear(activity, object : MyDayMonthYear {
                override fun onPickDayMonthYear(
                    dayName: String?,
                    monthYear: String?,
                    date: String?
                ) {
                    if (dayName != null) {
                        this@AddExpenseFragment.dayName = dayName
                        if (monthYear != null) {
                            this@AddExpenseFragment.monthAndYear = monthYear
                        }
                        if (date != null) {
                            this@AddExpenseFragment.date = date
                        }
                        binding.dateTv.text = dayName + " " + date
                    }
                    Log.d(TAG, "monthAndYear: $monthAndYear")
                }
            }
            )
        }
    }

    private fun addExpense(
        amount: String,
        description: String,
    ) {
        val accountId = session.getString("ACCOUNT_ID", "").toString()
        val timestamp = "" + System.currentTimeMillis()
        val expenditure =
            Expense(
                timestamp,
                selectedUid,
                date,
                monthAndYear,
                selectedId,
                selectedName,
                amount,
                description,
                timestamp,
                timestamp
            )
        database.child(accountId).child("Expense").child(timestamp)
            .setValue(expenditure)
        Toast.makeText(
            context,
            "Expense Added Successful",
            Toast.LENGTH_SHORT,
        ).show()
    }

    private fun setupSpinner(it: List<User>) {
            if (it.isNotEmpty()) {
                val adapter = MemberSpAdapter(requireContext(), it)
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
                            val selectedItem: User = parent?.getItemAtPosition(position) as User
                            selectedUid = selectedItem.uid.toString()
                            selectedId = selectedItem.id.toString()
                            selectedName = selectedItem.name.toString()
                        }

                    }

            } else {
                Toast.makeText(context, "No Data Found", Toast.LENGTH_LONG).show()
            }
    }

    private fun getMembers() {
        val accountId = session.getString("ACCOUNT_ID", "").toString()
        memberViewModel.getMembers(accountId)

        memberViewModel.memberLiveData.observe(viewLifecycleOwner) {
            binding.mainLl.isVisible = false
            binding.progressBar.isVisible = false
            when (it) {
                is NetworkResult.Success -> {
                    setupSpinner(it.data!!)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
//202