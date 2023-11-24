package com.healingapp.triphealing.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.healingapp.triphealing.R
import com.healingapp.triphealing.databinding.FragmentSettingBinding
import com.healingapp.triphealing.databinding.FragmentTripBinding
import com.healingapp.triphealing.secret.Secret
import com.healingapp.triphealing.viewmodel.post_all.NetworkViewModel
import com.healingapp.triphealing.viewmodel.user.UserViewModel

class TripFragment : Fragment() {

    private var _binding: FragmentTripBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModelPost: NetworkViewModel
    private lateinit var viewModelUser: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTripBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelPost = ViewModelProvider(requireActivity())[NetworkViewModel::class.java]
        viewModelUser = ViewModelProvider(requireActivity())[UserViewModel::class.java]

        viewModelUser.getNetworkUserResponseLiveData().observe(viewLifecycleOwner, Observer { response ->
            if (response != null && response.code == "0000"){
                binding.tvTrip.text = HtmlCompat.fromHtml("<b>${response.userInfo.nickname}님!</b> 어디로 떠날까요?", HtmlCompat.FROM_HTML_MODE_LEGACY)
            }

            else{
                binding.tvTrip.text = HtmlCompat.fromHtml("<b>Guest님!<b> 어디로 떠날까요?", HtmlCompat.FROM_HTML_MODE_LEGACY)
            }

        })


    }

}