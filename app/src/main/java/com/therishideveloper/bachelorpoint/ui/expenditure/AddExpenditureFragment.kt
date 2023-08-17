package com.therishideveloper.bachelorpoint.ui.expenditure

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.therishideveloper.bachelorpoint.R
import com.therishideveloper.bachelorpoint.adapter.spinner.MemberSpAdapter
import com.therishideveloper.bachelorpoint.databinding.FragmentAddExpenditureBinding
import com.therishideveloper.bachelorpoint.model.Expense
import com.therishideveloper.bachelorpoint.model.User
import com.therishideveloper.bachelorpoint.ui.member.MemberViewModel
import com.therishideveloper.bachelorpoint.utils.MyCalender


class AddExpenditureFragment : Fragment() {

    private val TAG = "AddExpenditureFragment"

    private var _binding: FragmentAddExpenditureBinding? = null
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddExpenditureBinding.inflate(inflater, container, false)

        auth = Firebase.auth
        database = Firebase.database.reference.child(getString(R.string.app_name)).child("Users")
        session = requireContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.dateTv.text = (MyCalender.dayToday + " " + MyCalender.currentDate)

        setupSpinner()

        binding.saveBtn.setOnClickListener {

            amount = binding.amountEt.text.toString().trim()
            description = binding.descriptionEt.text.toString().trim()

            addExpenditureAccount(
                amount,
                description
            )

        }

    }

    private fun addExpenditureAccount(
        amount: String,
        description: String,
    ) {
        val accountId = session.getString("ACCOUNT_ID", "").toString()
        val timestamp = "" + System.currentTimeMillis()
        val expenditure =
            Expense(
                timestamp,
                selectedUid,
                MyCalender.currentDate,
                MyCalender.currentMonthYear,
                selectedId,
                selectedName,
                amount,
                description,
                timestamp,
                timestamp
            )
        database.child(accountId).child("Expenditure").child(timestamp)
            .setValue(expenditure)
        Toast.makeText(
            context,
            "Expenditure Added Successful",
            Toast.LENGTH_SHORT,
        ).show()
    }

    private fun setupSpinner() {
        memberViewModel.data.observe(viewLifecycleOwner) {
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