package com.therishideveloper.bachelorpoint.ui.member

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.therishideveloper.bachelorpoint.adapter.MemberAdapter
import com.therishideveloper.bachelorpoint.databinding.FragmentMemberBinding
import com.therishideveloper.bachelorpoint.model.User
import kotlin.math.log

class MemberFragment : Fragment() {

    private val TAG = "MemberFragment"

    private var _binding: FragmentMemberBinding? = null
    private val binding get() = _binding!!
    private val memberViewModel: MemberViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMemberBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        memberViewModel.data.observe(viewLifecycleOwner) {
            val adapter = MemberAdapter(findNavController(),it)
            binding.recyclerView.adapter = adapter
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}