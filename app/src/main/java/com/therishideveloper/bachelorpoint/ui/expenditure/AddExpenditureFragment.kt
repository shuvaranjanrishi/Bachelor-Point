package com.therishideveloper.bachelorpoint.ui.expenditure

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.therishideveloper.bachelorpoint.Utils.MyCalender
import com.therishideveloper.bachelorpoint.adapter.spinner.MemberSpinnerAdapter
import com.therishideveloper.bachelorpoint.databinding.FragmentAddExpenditureBinding
import com.therishideveloper.bachelorpoint.listener.MyDate
import com.therishideveloper.bachelorpoint.model.Member
import com.therishideveloper.bachelorpoint.ui.member.MemberViewModel

class AddExpenditureFragment : Fragment() {

    private var _binding: FragmentAddExpenditureBinding? = null
    private val binding get() = _binding!!

    private val memberViewModel: MemberViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddExpenditureBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.dateTv.text = (MyCalender.dayToday +" "+MyCalender.currentDate)

        setupSpinner()

//        val name: String = binding.nameEt.text.toString().trim()
//        val email: String = binding.emailEt.text.toString().trim()
//        val address: String = binding.addressEt.text.toString().trim()
//        val phone: String = binding.phoneEt.text.toString().trim()
//
//        binding.saveBtn.setOnClickListener {
//            val timestamp: String = "" + System.currentTimeMillis();
//            val member = Member("1", name, email, address, phone, timestamp, timestamp)
//            Log.d("AddMember", "Member: $member")
//        }
    }

    private fun setupSpinner() {
        memberViewModel.data.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                val adapter = MemberSpinnerAdapter(requireContext(), it)
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
                            val selectedItem: Member = parent?.getItemAtPosition(position) as Member
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