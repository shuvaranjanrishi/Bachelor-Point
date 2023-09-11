package com.therishideveloper.bachelorpoint.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.DatabaseReference
import com.therishideveloper.bachelorpoint.R
import com.therishideveloper.bachelorpoint.adapter.ModuleAdapter
import com.therishideveloper.bachelorpoint.databinding.FragmentHomeBinding
import com.therishideveloper.bachelorpoint.reference.DBRef
import com.therishideveloper.bachelorpoint.session.UserSession
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val TAG = "HomeFragment"

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel: HomeViewModel by viewModels()
    @Inject
    lateinit var session: UserSession
    @Inject
    lateinit var dbRef: DBRef
    private lateinit var database: DatabaseReference
    private lateinit var accountId: String
    private lateinit var userType: String
    private lateinit var userId: String

    private val rotateOpen: Animation by lazy {
        AnimationUtils.loadAnimation(
            activity,
            R.anim.rotate_open_anim
        )
    }
    private val rotateClose: Animation by lazy {
        AnimationUtils.loadAnimation(
            activity,
            R.anim.rotate_close_anim
        )
    }
    private val fromBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            activity,
            R.anim.from_bottom_anim
        )
    }
    private val toBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            activity,
            R.anim.to_bottom_anim
        )
    }
    private val fromRight: Animation by lazy {
        AnimationUtils.loadAnimation(
            activity,
            R.anim.from_right_anim
        )
    }
    private val toRight: Animation by lazy {
        AnimationUtils.loadAnimation(
            activity,
            R.anim.to_right_anim
        )
    }

    private var clicked = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        database = dbRef.getAccountRef()
        accountId = session.getAccountId().toString()
        userId = session.getUid().toString()
        userType = session.getUserType().toString()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (userType != "Admin"){
            binding.fab.visibility = View.GONE
        }
        binding.fab.setOnClickListener {
            setVisibility(clicked)
            setAnimation(clicked)
            clicked = !clicked
        }

        binding.addMemberBtn.setOnClickListener {
            findNavController().navigate(R.id.action_nav_home_to_nav_add_member)
        }
        binding.addMealBtn.setOnClickListener {
            findNavController().navigate(R.id.action_nav_home_to_nav_add_meal)
        }
        binding.addExpenditureBtn.setOnClickListener {
            findNavController().navigate(R.id.action_nav_home_to_nav_add_expenditure)
        }
        binding.addRentBtn.setOnClickListener {
            findNavController().navigate(R.id.action_nav_home_to_nav_add_rent)
        }

        homeViewModel.data.observe(viewLifecycleOwner) {
            val adapter = ModuleAdapter(requireContext(),findNavController(), it)
            binding.recyclerView.adapter = adapter
        }

    }

    private fun setAnimation(clicked: Boolean) {
        if (!clicked) {
            binding.addMemberBtn.startAnimation(fromRight)
            binding.addMealBtn.startAnimation(fromBottom)
            binding.addExpenditureBtn.startAnimation(fromBottom)
            binding.addRentBtn.startAnimation(fromBottom)
            binding.fab.startAnimation(rotateOpen)
        } else {
            binding.addMemberBtn.startAnimation(toRight)
            binding.addMealBtn.startAnimation(toBottom)
            binding.addExpenditureBtn.startAnimation(toBottom)
            binding.addRentBtn.startAnimation(toBottom)
            binding.fab.startAnimation(rotateClose)
        }
    }

    private fun setVisibility(clicked: Boolean) {
        if (!clicked) {
            binding.addMemberBtn.visibility = View.VISIBLE
            binding.addMealBtn.visibility = View.VISIBLE
            binding.addRentBtn.visibility = View.VISIBLE
            binding.addExpenditureBtn.visibility = View.VISIBLE
        } else {
            binding.addMemberBtn.visibility = View.GONE
            binding.addMealBtn.visibility = View.GONE
            binding.addRentBtn.visibility = View.GONE
            binding.addExpenditureBtn.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        clicked = false
    }
}