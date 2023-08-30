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
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.therishideveloper.bachelorpoint.R
import com.therishideveloper.bachelorpoint.adapter.AddSeparateRentAdapter
import com.therishideveloper.bachelorpoint.adapter.spinner.RentTypeSpAdapter
import com.therishideveloper.bachelorpoint.api.NetworkResult
import com.therishideveloper.bachelorpoint.databinding.FragmentAddRentBinding
import com.therishideveloper.bachelorpoint.listener.AddRentListener
import com.therishideveloper.bachelorpoint.listener.MyMonthAndYear
import com.therishideveloper.bachelorpoint.model.Rent
import com.therishideveloper.bachelorpoint.model.SeparateRent
import com.therishideveloper.bachelorpoint.model.User
import com.therishideveloper.bachelorpoint.ui.member.MemberViewModel
import com.therishideveloper.bachelorpoint.utils.MyCalender
import dagger.hilt.android.AndroidEntryPoint
import java.math.RoundingMode
import java.text.DecimalFormat

@AndroidEntryPoint
class AddRentFragment : Fragment() , AddRentListener{

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
    private lateinit var decimalFormat: DecimalFormat
    private lateinit var rentList: List<SeparateRent>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddRentBinding.inflate(inflater, container, false)

        auth = Firebase.auth
        database = Firebase.database.reference.child(getString(R.string.app_name)).child("Accounts")
        session = requireContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        decimalFormat = DecimalFormat("#.##")
        decimalFormat.roundingMode = RoundingMode.UP

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSpinner()

        setupDatePicker()

        getMembers()

        binding.amountEt.addTextChangedListener(
            onTextChanged = { _, _, _, _ ->
                try {
                    val inputs = binding.amountEt.text.toString().trim().toDouble()
                    val totalMember = binding.totalMemberTv.text.toString().trim().toDouble()
                    if (inputs != 0.0 && totalMember != 0.0) {
                        val costPerHead = (inputs) / (totalMember)
                        binding.perHeadCostTv.text = decimalFormat.format(costPerHead).toString()
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

    private fun getMembers() {
        val accountId = session.getString("ACCOUNT_ID", "").toString()
        memberViewModel.getMembers(accountId)

        memberViewModel.memberLiveData.observe(viewLifecycleOwner) {
            binding.mainLl.isVisible = false
            binding.progressBar.isVisible = false
            when (it) {
                is NetworkResult.Success -> {
                    this@AddRentFragment.memberList = it.data!!
                    binding.mainLl.isVisible = true
                    binding.totalMemberTv.text = memberList.size.toString()
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

        if (selectedId != "1") {
            database.child(accountId).child("RentAndBill").child("Bill").child(monthAndYear)
                .child(selectedId)
                .setValue(rent)
        } else {
            if (binding.checkBox.isChecked) {
                if (rentList.isNotEmpty()) {
                    Log.d(TAG, "rentList: $rentList")
                    for (separateRent in rentList) {
                        database.child(accountId).child("RentAndBill").child("Rent").child(monthAndYear)
                            .child(separateRent.id.toString())
                            .setValue(separateRent)
                    }
                }
            } else {
                if (memberList.isNotEmpty()) {
                    Log.d(TAG, "rentList: $memberList")
                    for (separateRent in memberList) {
                        val amt = amount.toDouble() / memberList.size
                        val rent1 = SeparateRent(
                            "" + separateRent.id.toString(),
                            "" + separateRent.name.toString(),
                            "" + amt,
                            "" + timestamp,
                            "" + timestamp,
                        )
                        database.child(accountId).child("RentAndBill").child("Rent").child(monthAndYear)
                            .child(rent1.id.toString())
                            .setValue(rent1)
                    }
                }
            }
        }

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
                            if(selectedId=="8"){
                                binding.descriptionEt.visibility = View.VISIBLE
                            }else{
                                binding.descriptionEt.visibility = View.GONE
                            }
                            if(selectedId=="1"){
                                binding.checkBoxLl.visibility = View.VISIBLE
                                setupCheckbox()
                            }else{
                                binding.checkBoxLl.visibility = View.GONE
                                binding.checkBox.isChecked = false
                            }
                        }

                    }

            } else {
                Toast.makeText(context, "No Data Found", Toast.LENGTH_LONG).show()
            }

        }
    }

    private fun setupCheckbox() {
        binding.checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                binding.separateRentLl.visibility = View.VISIBLE
                binding.perHeadCostLl.visibility = View.GONE
                binding.amountEt.isEnabled = false
                binding.amountEt.setText("0")
                binding.perHeadCostTv.text = "0"
                setupSeparateRentInputAdapter()
            }else{
                binding.separateRentLl.visibility = View.GONE
                binding.perHeadCostLl.visibility = View.VISIBLE
                binding.amountEt.isEnabled = true
            }
        }
    }

    private fun setupSeparateRentInputAdapter() {
        val listener = this
//        memberViewModel.data.observe(viewLifecycleOwner) {
//            Log.d("TAG", "mealList.size: " + it.size.toString())
            val rentList: MutableList<SeparateRent> = mutableListOf()
            for (user in memberList) {
                val rent = SeparateRent(
                    "" + user.id,
                    "" + user.name,
                    "0",
                    "" + System.currentTimeMillis(),
                    "" + System.currentTimeMillis()
                )
                rentList.add(rent)
                Log.d(TAG, "onDataChange: mealList: $rentList")
            }
            val adapter = AddSeparateRentAdapter(listener,rentList)
            binding.recyclerView.adapter = adapter
//        }
    }

    override fun onChangeRent(rentList: List<SeparateRent>) {
        this.rentList = rentList
        if (rentList.isNotEmpty()) {
            var totalRent = 0.0
            for (meal in rentList) {
                totalRent += meal.separateRent!!.toDouble();
                binding.amountEt.setText(decimalFormat.format(totalRent).toString())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
