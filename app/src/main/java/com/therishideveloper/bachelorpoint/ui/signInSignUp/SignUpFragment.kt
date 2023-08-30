package com.therishideveloper.bachelorpoint.ui.signInSignUp

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.therishideveloper.bachelorpoint.R
import com.therishideveloper.bachelorpoint.databinding.FragmentSignUpBinding
import com.therishideveloper.bachelorpoint.model.User

class SignUpFragment : Fragment() {

    private val TAG = "SignUpAdminFragment"

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var session: SharedPreferences

    private lateinit var name: String
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var address: String
    private lateinit var phone: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
        database = Firebase.database.reference.child(getString(R.string.app_name))

        binding.signUpBtn.setOnClickListener {

            name = binding.nameEt.text.toString().trim()
            email = binding.emailEt.text.toString().trim()
            password = binding.passwordEt.text.toString().trim()
            address = binding.addressEt.text.toString().trim()
            phone = binding.phoneEt.text.toString().trim()

            if (validate()) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            val uid = auth.currentUser!!.uid
                            createAdminAccount(uid)
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
        }
    }

    private fun createAdminAccount(uid: String) {
        val timestamp = "" + System.currentTimeMillis()
        val user =
            User(
                "" + timestamp,
                "" + uid,
                "" + uid,
                "" + name,
                "" + phone,
                "" + address,
                "" + email,
                "" + password,
                "Admin",
                "true",
                "" + timestamp,
                "" + timestamp
            )
        database.child("Users").child(uid).setValue(user)
            .addOnCompleteListener {
                database.child("Accounts").child(auth.uid.toString()).child("Members").child(uid).setValue(user)
                    .addOnCompleteListener{
                        if (it.isSuccessful) {
                            Toast.makeText(
                                context,
                                "Sign Up Successful",
                                Toast.LENGTH_SHORT,
                            ).show()
                            findNavController().navigate(R.id.action_nav_sign_up_admin_to_nav_sign_in)
                        } else {
                            Log.e("addOnCompleteListener", "" + it.exception)
                        }
                    }
            }
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

