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
    lateinit var session: SharedPreferences

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
        database = Firebase.database.reference.child("BachelorPoint")

        binding.signUpBtn.setOnClickListener {

            val name: String = binding.nameEt.text.toString().trim()
            val email: String = binding.emailEt.text.toString().trim()
            val password: String = binding.passwordEt.text.toString().trim()

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        val uid = auth.currentUser!!.uid
                        createAdminAccount(uid, name, email, password)
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

    private fun createAdminAccount(
        uid: String,
        name: String,
        email: String,
        password: String,
    ) {
        val timestamp = "" + System.currentTimeMillis()
        val user =
            User(
                timestamp,
                uid,
                uid,
                name,
                "",
                "",
                email,
                password,
                "Admin",
                "true",
                timestamp,
                timestamp
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

