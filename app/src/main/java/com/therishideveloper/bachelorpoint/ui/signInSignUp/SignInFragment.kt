package com.therishideveloper.bachelorpoint.ui.signInSignUp

import android.content.Intent
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
import com.therishideveloper.bachelorpoint.MainActivity
import com.therishideveloper.bachelorpoint.R
import com.therishideveloper.bachelorpoint.api.NetworkResult
import com.therishideveloper.bachelorpoint.databinding.FragmentSignInBinding
import com.therishideveloper.bachelorpoint.model.User
import com.therishideveloper.bachelorpoint.reference.DBRef
import com.therishideveloper.bachelorpoint.session.UserSession
import com.therishideveloper.bachelorpoint.ui.member.MemberViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignInFragment : Fragment() {

    private val TAG = "SignInFragment"

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    @Inject
    lateinit var dbRef: DBRef
    private lateinit var database: DatabaseReference
    private val authViewModel: AuthViewModel by viewModels()
    private val memberViewModel: MemberViewModel by viewModels()
    @Inject
    lateinit var session: UserSession

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        auth = Firebase.auth
        database = dbRef.getDbRef()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signInBtn.setOnClickListener {
            val email: String = binding.emailEt.text.toString().trim()
            val password: String = binding.passwordEt.text.toString().trim()

            if(email.isEmpty()){
                binding.emailEt.error = "Plz Enter Your Email"
                binding.emailEt.requestFocus()
            }else if (password.isEmpty()){
                binding.passwordEt.error = "Plz Enter Your Password"
                binding.passwordEt.requestFocus()
            }else{
                binding.signInBtn.isVisible = false
                authViewModel.signIn(email, password, auth)
            }
        }

        binding.createNewTv.setOnClickListener {
            findNavController().navigate(R.id.action_nav_sign_in_to_nav_sign_up)
        }

        getSignInResponse()
    }

    private fun getSignInResponse() {

        authViewModel.singInResponseLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.isVisible = false
            when (it) {
                is NetworkResult.Success -> {
                    memberViewModel.getMember(it.data!!)
                    getMemberResponse()
                }

                is NetworkResult.Error -> {
                    Toast.makeText(context, "Error: ${it.message}", Toast.LENGTH_LONG).show()
                    binding.signInBtn.isVisible = true
                }

                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true
                }
            }
        }

    }

    private fun getMemberResponse() {
        memberViewModel.memberLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.isVisible = false
            when (it) {
                is NetworkResult.Success -> {
                    saveUserInfo(it.data!!)
                }

                is NetworkResult.Error -> {
                    Toast.makeText(context, "Error: ${it.message}", Toast.LENGTH_LONG).show()
                }

                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true
                }
            }
        }

    }

    private fun saveUserInfo(user: User) {
        session.setUid(user.uid!!)
        session.setUserName(user.name!!)
        session.setEmail(user.email!!)
        session.setUserType(user.usertype!!)
        session.setAccountId(user.accountId!!)
        session.setId(user.id!!)

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