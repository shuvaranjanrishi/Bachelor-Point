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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.therishideveloper.bachelorpoint.R
import com.therishideveloper.bachelorpoint.adapter.ModuleAdapter
import com.therishideveloper.bachelorpoint.databinding.FragmentSignInBinding
import com.therishideveloper.bachelorpoint.model.User
import com.therishideveloper.bachelorpoint.session.SessionManager
import com.therishideveloper.bachelorpoint.ui.home.HomeViewModel

class SignInFragment : Fragment() {

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
        database = Firebase.database.reference.child(getString(R.string.app_name))

        binding.signInBtn.setOnClickListener {

            val email: String = binding.emailEt.text.toString().trim()
            val password: String = binding.passwordEt.text.toString().trim()

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        val userid = auth.currentUser!!.uid
                        getUserInfo(userid)
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

        homeViewModel.data.observe(viewLifecycleOwner) {
            val adapter = ModuleAdapter(findNavController(), it)
//            binding.recyclerView.adapter = adapter
        }

    }

    private fun getUserInfo(userid: String) {
        database.child(userid).child("Members").orderByChild("userId").equalTo(userid)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (ds in dataSnapshot.children) {
                            val user: User? = ds.getValue<User>()
                            val name: String = "" + ds.child("name")
                            val email: String = "" + ds.child("email")
                            val id: String = "" + ds.child("id")
                            val userId: String = "" + ds.child("userId")
                            val usertype: String = "" + ds.child("usertype")
                            Log.d("LoginUser: ", user.toString())
                            saveUserInfo(name, email, id, userId, usertype)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.w("CheckUserType", "Failed to read value.", error.toException())
                    }
                }
            )
    }

    private fun saveUserInfo(
        name: String,
        email: String,
        id: String,
        userId: String,
        usertype: String
    ) {
        val session = context?.let { SessionManager(it) }!!
        session.setName(name)
        session.setEmail(email)
        session.setUserType(usertype)
        Toast.makeText(
            context,
            "Sign In Successful",
            Toast.LENGTH_SHORT,
        ).show()
        findNavController().navigate(R.id.action_nav_sign_in_to_nav_home)
    }

}