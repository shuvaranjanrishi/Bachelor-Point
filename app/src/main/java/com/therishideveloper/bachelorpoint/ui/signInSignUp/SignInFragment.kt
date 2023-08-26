package com.therishideveloper.bachelorpoint.ui.signInSignUp

import android.content.Context
import android.content.Intent
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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.therishideveloper.bachelorpoint.MainActivity
import com.therishideveloper.bachelorpoint.R
import com.therishideveloper.bachelorpoint.databinding.FragmentSignInBinding
import com.therishideveloper.bachelorpoint.model.User
import com.therishideveloper.bachelorpoint.ui.home.HomeViewModel

class SignInFragment : Fragment() {

    private val TAG = "SignInFragment"

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var session: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        auth = Firebase.auth
        database = Firebase.database.reference.child(getString(R.string.app_name))
        session = requireContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        editor = session.edit()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signInBtn.setOnClickListener {

            val email: String = binding.emailEt.text.toString().trim()
            val password: String = binding.passwordEt.text.toString().trim()

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        val userid = auth.currentUser!!.uid
                        getUserInfo(userid)
                    } else {
                        Log.e(TAG, "addOnCompleteListener" + it.exception)
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

        binding.createNewTv.setOnClickListener {
            findNavController().navigate(R.id.action_nav_sign_in_to_nav_sign_up)
        }

    }

    private fun getUserInfo(uid: String) {
        database.child("Users").orderByChild("uid").equalTo(uid)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (ds in dataSnapshot.children) {
                            val user: User? = ds.getValue<User>()

                            if (user != null) {
                                Log.e(TAG, "LoginUser: $user")
                                saveUserInfo(
                                    user.name!!,
                                    user.email!!,
                                    user.id!!,
                                    user.uid!!,
                                    user.accountId!!,
                                    user.usertype!!
                                )
                            } else {
                                Toast.makeText(
                                    context,
                                    "User Info not Found!",
                                    Toast.LENGTH_SHORT,
                                ).show()
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e(TAG, "getUserInfo" + error.toException())
                    }
                }
            )
    }

    private fun saveUserInfo(
        name: String,
        email: String,
        id: String,
        uid: String,
        accountId: String,
        usertype: String
    ) {

        editor.putString("USER_ID", "" + uid);
        editor.putString("USER_TYPE", "" + usertype)
        editor.putString("ACCOUNT_ID", "" + accountId)
        editor.putString("MEMBER_ID", "" + id)
        editor.apply()

        Toast.makeText(
            context,
            "Sign In Successful",
            Toast.LENGTH_SHORT,
        ).show()
        requireContext().startActivity(
            Intent(
                context,
                MainActivity::class.java
            ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        )
        requireActivity().finish()
    }

}