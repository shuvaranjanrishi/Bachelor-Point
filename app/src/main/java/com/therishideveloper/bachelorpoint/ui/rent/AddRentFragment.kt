package com.therishideveloper.bachelorpoint.ui.rent

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.therishideveloper.bachelorpoint.R
import com.therishideveloper.bachelorpoint.adapter.spinner.RentTypeSpAdapter
import com.therishideveloper.bachelorpoint.databinding.FragmentAddRentBinding
import com.therishideveloper.bachelorpoint.listener.MyMonthAndYear
import com.therishideveloper.bachelorpoint.model.Rent
import com.therishideveloper.bachelorpoint.model.User
import com.therishideveloper.bachelorpoint.ui.member.MemberViewModel
import com.therishideveloper.bachelorpoint.utils.MyCalender

class AddRentFragment : Fragment() {

    private val TAG = "AddRentFragment"

    private var _binding: FragmentAddRentBinding? = null
    private val binding get() = _binding!!

    private val memberViewModel: MemberViewModel by viewModels()
    private val rentViewModel: RentViewModel by viewModels()
    private var memberList: List<User> = mutableListOf()

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var session: SharedPreferences
    private lateinit var selectedId: String
    private lateinit var selectedName: String
    private lateinit var amount: String
    private lateinit var monthAndYear: String
    private lateinit var date: String
    private lateinit var description: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddRentBinding.inflate(inflater, container, false)

        auth = Firebase.auth
        database = Firebase.database.reference.child(getString(R.string.app_name)).child("Users")
        session = requireContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSpinner()

        setupDatePicker()

        memberViewModel.data.observe(viewLifecycleOwner) {
            memberList = it
            binding.totalMemberTv.text = memberList.size.toString()
        }

        binding.amountEt.addTextChangedListener(
            onTextChanged = { _, _, _, _ ->
                try {
                    val inputs = binding.amountEt.text.toString().trim().toInt()
                    val totalMember = binding.totalMemberTv.text.toString().trim().toInt()
                    if (inputs != 0 && totalMember != 0) {
                        val costPerHead = (inputs) / (totalMember)
                        binding.perHeadCostTv.text = costPerHead.toString()
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Exception: ${e.message}")
                }
            }
        )

        binding.saveBtn.setOnClickListener {
            amount = binding.amountEt.text.toString().trim()
            description = binding.descriptionEt.text.toString().trim()
            addRentAndBill(amount, description)
        }
    }

    private fun setupDatePicker() {
        binding.dateTv.text = MyCalender.currentMonthYear
        monthAndYear = MyCalender.currentMonthYear
        date = MyCalender.currentDate
        binding.dateTv.setOnClickListener {
            MyCalender.pickMonthAndYear(activity, object : MyMonthAndYear {
                override fun onPickMonthAndYear(monthAndYear: String?) {
                    binding.dateTv.text = monthAndYear
                    if (monthAndYear != null) {
                        this@AddRentFragment.monthAndYear = monthAndYear
                    }
                    Log.d(TAG, "monthAndYear: $monthAndYear")
                }

                override fun onPickDate(date: String?) {
                    if (date != null) {
                        this@AddRentFragment.date = date
                    }
                    Log.d(TAG, "date: $date")
                }
            })
        }
    }

    private fun addRentAndBill(amount: String, description: String) {
        val accountId = session.getString("ACCOUNT_ID", "").toString()
        val timestamp = "" + System.currentTimeMillis()
        val rent =
            Rent(
                selectedId,
                selectedName,
                amount,
                monthAndYear,
                date,
                description,
                timestamp,
                timestamp
            )
        database.child(accountId).child("RentAndBill").child(timestamp)
            .setValue(rent)
        Toast.makeText(
            context,
            "Rent And Bill Added Successful",
            Toast.LENGTH_SHORT,
        ).show()
    }

    private fun setupSpinner() {
        rentViewModel.data.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                val adapter = RentTypeSpAdapter(requireContext(), it)
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
                            val selectedItem: Rent = parent?.getItemAtPosition(position) as Rent
                            selectedId = selectedItem.id.toString()
                            selectedName = selectedItem.name.toString()
                            Toast.makeText(
                                context,
                                "" + selectedItem.name + " Id: " + selectedItem.id,
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    }

            } else {
                Toast.makeText(context, "No Data Found", Toast.LENGTH_LONG).show()
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
