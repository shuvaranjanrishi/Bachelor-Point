package com.therishideveloper.bachelorpoint.ui.member

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.therishideveloper.bachelorpoint.adapter.MemberAdapter
import com.therishideveloper.bachelorpoint.databinding.FragmentAddMemberBinding
import com.therishideveloper.bachelorpoint.model.Member

class AddMemberFragment : Fragment() {

    private var _binding: FragmentAddMemberBinding? = null
    private val binding get() = _binding!!

    private val memberViewModel: MemberViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddMemberBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val name: String = binding.nameEt.text.toString().trim()
        val email: String = binding.emailEt.text.toString().trim()
        val address: String = binding.addressEt.text.toString().trim()
        val phone: String = binding.phoneEt.text.toString().trim()

        binding.saveBtn.setOnClickListener {
            val timestamp: String = "" + System.currentTimeMillis();
            val member = Member("1", name, email, address, phone, timestamp, timestamp)
            Log.d("AddMember", "Member: $member")
        }

        memberViewModel.data.observe(viewLifecycleOwner) {
//            val adapter = MemberAdapter(it)
//            binding.recyclerView.adapter = adapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}