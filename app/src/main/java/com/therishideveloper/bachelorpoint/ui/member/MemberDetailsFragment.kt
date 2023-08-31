package com.therishideveloper.bachelorpoint.ui.member

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.therishideveloper.bachelorpoint.api.NetworkResult
import com.therishideveloper.bachelorpoint.databinding.FragmentMemberDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MemberDetailsFragment : Fragment() {

    private val TAG = "MemberDetailsFragment"

    private var _binding: FragmentMemberDetailsBinding? = null
    private val binding get() = _binding!!

    private val memberViewModel: MemberViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMemberDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val uid = arguments?.getString("UID")
        memberViewModel.getMember(uid.toString())

        getMemberResponse()
    }

    private fun getMemberResponse() {
        memberViewModel.memberLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.isVisible = false
            binding.mainLl.isVisible = false
            when (it) {
                is NetworkResult.Success -> {
                    val member = it.data!!
                    binding.nameTv.text = member.name
                    binding.emailTv.text = member.email
                    binding.phoneTv.text = member.phone
                    binding.addressTv.text = member.address
                    binding.mainLl.isVisible = true
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}