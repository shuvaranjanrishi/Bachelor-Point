package com.therishideveloper.bachelorpoint.ui.member

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.therishideveloper.bachelorpoint.R
import com.therishideveloper.bachelorpoint.databinding.FragmentMemberDetailsBinding
import com.therishideveloper.bachelorpoint.model.User


class MemberDetailsFragment : Fragment() {

    private val TAG = "MemberDetailsFragment"

    private var _binding: FragmentMemberDetailsBinding? = null
    private val binding get() = _binding!!

    private val memberViewModel: MemberViewModel by viewModels()
    private lateinit var session: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMemberDetailsBinding.inflate(inflater, container, false)
        session = requireContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val database: DatabaseReference =
            Firebase.database.reference.child(getString(R.string.database_name)).child("Users")
        val accountId = arguments?.getString("UID")

        database.orderByChild("uid").equalTo(accountId)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        var member: User? = null
                        for (ds in dataSnapshot.children) {
                            member = ds.getValue(User::class.java)!!
                            Log.d(TAG, "onDataChange: $member")
                        }
                        if (member!=null){
                            binding.nameTv.text = member.name
                            binding.emailTv.text = member.email
                            binding.phoneTv.text = member.phone
                            binding.addressTv.text = member.address
                        }

                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e(TAG, "DatabaseError", error.toException())
                    }
                }
            )

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}