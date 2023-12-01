package com.healingapp.triphealing.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.healingapp.triphealing.LoginActivity
import com.healingapp.triphealing.R
import com.healingapp.triphealing.databinding.FragmentSettingBinding
import com.healingapp.triphealing.secret.Secret
import com.healingapp.triphealing.viewmodel.post_all.NetworkViewModel
import com.healingapp.triphealing.viewmodel.user.UserViewModel


class SettingFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null
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
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvSetting.text = HtmlCompat.fromHtml("<b>설정</b>",
            HtmlCompat.FROM_HTML_MODE_LEGACY)

        viewModelPost = ViewModelProvider(requireActivity())[NetworkViewModel::class.java]
        viewModelUser = ViewModelProvider(requireActivity())[UserViewModel::class.java]

        binding.loginbtn.setOnClickListener {
            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }


        viewModelUser.getNetworkUserResponseLiveData().observe(viewLifecycleOwner, Observer { response ->
            if (response != null && response.code == "0000"){
                //Log.e("TEST USER INFO", response.userInfo.toString())
                Glide.with(this)
                    .load(Secret.USER_MEDIA_URL+response.userInfo.profileImg)
                    .error(R.drawable.group_24)
                    .into(binding.imgProfile)

                binding.tvNickname.text = response.userInfo.nickname
                binding.tvIntroduce.text = response.userInfo.introduceText
            }

            else{
                binding.tvNickname.text = "GUEST"
                binding.tvIntroduce.text = "로그인이 필요합니다."

                Glide.with(this)
                    .load(R.drawable.group_24)
                    .error(R.drawable.group_24)
                    .into(binding.imgProfile)


            }

        })

    }

}