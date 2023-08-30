package com.therishideveloper.bachelorpoint.ui.signInSignUp

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.therishideveloper.bachelorpoint.R
import com.therishideveloper.bachelorpoint.api.NetworkResult
import com.therishideveloper.bachelorpoint.databinding.FragmentSignUpBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        auth = Firebase.auth
        database = Firebase.database.reference.child(getString(R.string.database_name))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signUpBtn.setOnClickListener {

            name = binding.nameEt.text.toString().trim()
            email = binding.emailEt.text.toString().trim()
            password = binding.passwordEt.text.toString().trim()
            address = binding.addressEt.text.toString().trim()
            phone = binding.phoneEt.text.toString().trim()

            if (validate()) {
                binding.signUpBtn.isVisible = false
                authViewModel.signUp(name, email, password, address, phone, auth, database)
            }
        }

        getSignUpResponse()
    }

    private fun getSignUpResponse() {

        authViewModel.singUpResponseLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.isVisible = false
            when (it) {
                is NetworkResult.Success -> {
                    findNavController().navigate(R.id.action_nav_sign_up_admin_to_nav_sign_in)
                }

                is NetworkResult.Error -> {
                    Toast.makeText(context, "Error: ${it.message}", Toast.LENGTH_LONG).show()
                    binding.signUpBtn.isVisible = true
                }

                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true
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

