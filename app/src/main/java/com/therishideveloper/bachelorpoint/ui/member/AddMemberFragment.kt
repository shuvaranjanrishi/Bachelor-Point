package com.therishideveloper.bachelorpoint.ui.member

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.therishideveloper.bachelorpoint.R
import com.therishideveloper.bachelorpoint.databinding.FragmentAddMemberBinding
import com.therishideveloper.bachelorpoint.model.User

class AddMemberFragment : Fragment() {

    private var _binding: FragmentAddMemberBinding? = null
    private val binding get() = _binding!!

    private val memberViewModel: MemberViewModel by viewModels()
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

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

        auth = Firebase.auth
        database = Firebase.database.reference.child(getString(R.string.app_name))

        binding.createMemberBtn.setOnClickListener {

            val name: String = binding.nameEt.text.toString().trim()
            val email: String = binding.emailEt.text.toString().trim()
            val password: String = binding.passwordEt.text.toString().trim()
            val address: String = binding.addressEt.text.toString().trim()
            val phone: String = binding.phoneEt.text.toString().trim()
            val adminId :String = auth.currentUser!!.uid
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        val userid = auth.currentUser!!.uid
                        createMemberAccount(adminId,userid, name, email, password)
                    } else {
                        Log.e("addOnCompleteListener", "" + it.exception)
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(
                        context,
                        "" + it.message,
                        Toast.LENGTH_SHORT,
                    ).show()
                }
        }
//        binding.saveBtn.setOnClickListener {
//            val timestamp: String = "" + System.currentTimeMillis();
//            val member = Member("1", name, email, address, phone, timestamp, timestamp)
//            Log.d("AddMember", "Member: $member")
//        }

        memberViewModel.data.observe(viewLifecycleOwner) {
//            val adapter = MemberAdapter(it)
//            binding.recyclerView.adapter = adapter
        }
    }

    private fun createMemberAccount(
        adminId: String,
        userId: String,
        name: String,
        email: String,
        password: String,
    ) {
        val timestamp = "" + System.currentTimeMillis()
        val user = User(timestamp, name, email, password, "Member", timestamp, timestamp)
        database.child(adminId).child("Members").child(userId).setValue(user)
        Toast.makeText(
            context,
            "Member Created Successful",
            Toast.LENGTH_SHORT,
        ).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}