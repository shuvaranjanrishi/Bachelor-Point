package com.therishideveloper.bachelorpoint.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.therishideveloper.bachelorpoint.R
import com.therishideveloper.bachelorpoint.Utils.MyCalender
import com.therishideveloper.bachelorpoint.adapter.ModuleAdapter
import com.therishideveloper.bachelorpoint.databinding.FragmentHomeBinding
import com.therishideveloper.bachelorpoint.listener.MyDate
import com.therishideveloper.bachelorpoint.session.SessionManager

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var session:SessionManager

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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        session = context?.let { SessionManager(it) }!!

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
            val adapter = ModuleAdapter(findNavController(), it)
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
        Log.d("sussssssss: ",session.getUserType().toString())
        if (!clicked) {
            binding.addMemberBtn.visibility = View.VISIBLE
            binding.addMealBtn.visibility = View.VISIBLE
            binding.addRentBtn.visibility = View.VISIBLE
            binding.addExpenditureBtn.visibility = View.VISIBLE
        } else {
            if(session.getUserType().equals("Admin")) binding.addMemberBtn.visibility = View.GONE
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