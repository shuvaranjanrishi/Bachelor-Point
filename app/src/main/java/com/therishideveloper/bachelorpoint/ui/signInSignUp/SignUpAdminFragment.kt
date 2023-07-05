package com.therishideveloper.bachelorpoint.ui.signInSignUp

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
import com.therishideveloper.bachelorpoint.adapter.ModuleAdapter
import com.therishideveloper.bachelorpoint.databinding.FragmentSignUpAdminBinding
import com.therishideveloper.bachelorpoint.model.User
import com.therishideveloper.bachelorpoint.ui.home.HomeViewModel

class SignUpAdminFragment : Fragment() {

    private var _binding: FragmentSignUpAdminBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpAdminBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
        database = Firebase.database.reference

        binding.signUpBtn.setOnClickListener {

            val name: String = binding.nameEt.text.toString().trim()
            val email: String = binding.emailEt.text.toString().trim()
            val password: String = binding.passwordEt.text.toString().trim()

            val timestamp: String = "" + System.currentTimeMillis();
            Log.d("onViewCreated", "name....$name $email")

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("onViewCreated", "createUserWithEmail:success")
                        val userid = auth.currentUser!!.uid
                        saveUser(userid, name, email, password, "Admin")
//                        updateUI(user)
                    } else {
                        Log.w("onViewCreated", "createUserWithEmail:failure", it.exception)
//                        updateUI(null)
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

        binding.signUpAsUserTv.setOnClickListener {
            findNavController().navigate(R.id.action_nav_nav_sign_up_admin_to_nav_home)
        }

        homeViewModel.data.observe(viewLifecycleOwner) {
            val adapter = ModuleAdapter(findNavController(), it)
//            binding.recyclerView.adapter = adapter
        }

    }

    private fun saveUser(
        userId: String,
        name: String,
        email: String,
        password: String,
        usertype: String
    ) {
        val user = User(name, email, password, usertype)
        database.child("Users").child(userId).setValue(user)
    }

}

