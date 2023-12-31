package com.therishideveloper.bachelorpoint.ui.member

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.therishideveloper.bachelorpoint.R
import com.therishideveloper.bachelorpoint.databinding.FragmentAddMemberBinding
import com.therishideveloper.bachelorpoint.model.User
import com.therishideveloper.bachelorpoint.reference.DBRef
import com.therishideveloper.bachelorpoint.session.UserSession
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AddMemberFragment : Fragment() {

    private val TAG = "AddMemberFragment"

    private var _binding: FragmentAddMemberBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    @Inject
    lateinit var session: UserSession

    @Inject
    lateinit var dbRef: DBRef
    private lateinit var userRef: DatabaseReference
    private lateinit var memberRef: DatabaseReference
    private lateinit var accountId: String

    private lateinit var name: String
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var address: String
    private lateinit var phone: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddMemberBinding.inflate(inflater, container, false)
        auth = Firebase.auth
        accountId = session.getAccountId().toString()
        userRef = dbRef.getUserRef()
        memberRef = dbRef.getMemberRef(accountId)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.createMemberBtn.setOnClickListener {

            name = binding.nameEt.text.toString().trim()
            email = binding.emailEt.text.toString().trim()
            password = binding.passwordEt.text.toString().trim()
            address = binding.addressEt.text.toString().trim()
            phone = binding.phoneEt.text.toString().trim()

            if (validate()) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            val userid = auth.currentUser!!.uid
                            createMemberAccount(
                                userid,
                            )
                        } else {
                            Log.e(TAG,"addOnCompleteListener: "+ it.exception)
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

    }

    private fun createMemberAccount(uid: String) {
        val timestamp = "" + System.currentTimeMillis()
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
        userRef.child(uid).setValue(user)
        memberRef.child(uid).setValue(user)
        Toast.makeText(
            context,
            "Member Created Successful",
            Toast.LENGTH_SHORT,
        ).show()
    }

    private fun validate(): Boolean {
        if (name.isEmpty()) {
            binding.nameEt.requestFocus()
            binding.nameEt.error = "Enter Your Name"
            return false
        }
        if (email.isEmpty()) {
            binding.emailEt.requestFocus()
            binding.emailEt.error = "Enter Your Email"
            return false
        }
        if (password.isEmpty()) {
            binding.passwordEt.requestFocus()
            binding.passwordEt.error = "Enter a Password"
            return false
        }
        if (password.length > 6) {
            binding.passwordEt.requestFocus()
            binding.passwordEt.error = "Password Length At Least 6 Character"
            return false
        }
        if (address.isEmpty()) {
            binding.addressEt.requestFocus()
            binding.addressEt.error = "Enter Your Address"
            return false
        }
        if (phone.isEmpty()) {
            binding.phoneEt.requestFocus()
            binding.phoneEt.error = "Enter Your Mobile Number"
            return false
        }
        if (!phone.startsWith("01")) {
            binding.phoneEt.requestFocus()
            binding.phoneEt.error = "Enter a Valid Mobile Number"
            return false
        }
        if (phone.length!=11) {
            binding.phoneEt.requestFocus()
            binding.phoneEt.error = "Enter a Valid Mobile Number"
            return false
        }
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}