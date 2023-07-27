package com.therishideveloper.bachelorpoint.ui.member

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.therishideveloper.bachelorpoint.R
import com.therishideveloper.bachelorpoint.adapter.MemberAdapter
import com.therishideveloper.bachelorpoint.databinding.FragmentAddMemberBinding
import com.therishideveloper.bachelorpoint.model.User

class AddMemberFragment : Fragment() {

    private val TAG = "AddMemberFragment"

    private var _binding: FragmentAddMemberBinding? = null
    private val binding get() = _binding!!

    private val memberViewModel: MemberViewModel by viewModels()
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var session: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddMemberBinding.inflate(inflater, container, false)
        session = requireContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE)

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
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        val userid = auth.currentUser!!.uid
                        createMemberAccount(
                            userid,
                            name,
                            phone,
                            address,
                            email,
                            password
                        )
                    } else {
                        Log.e("addOnCompleteListener", "" + it.exception)
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(
                        context,
                        "" + it.message,
                        Toast.LENGTH_LONG,
                    ).show()
                }
        }

    }

    private fun createMemberAccount(
        uid: String,
        name: String,
        phone: String,
        address: String,
        email: String,
        password: String,
    ) {
        val accountId = session.getString("ACCOUNT_ID", "").toString()
        val timestamp = "" + System.currentTimeMillis()
        Log.e(TAG, "createMemberAccount: $accountId $uid")
        val user =
            User(
                timestamp,
                uid,
                accountId,
                name,
                phone,
                address,
                email,
                password,
                "Member",
                "true",
                timestamp,
                timestamp
            )
        database.child("Users").child(uid).setValue(user)
        database.child("Users").child(accountId).child("Members").child(uid).setValue(user)
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