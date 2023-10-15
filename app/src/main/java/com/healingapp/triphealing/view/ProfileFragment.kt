package com.healingapp.triphealing.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.healingapp.triphealing.R
import com.healingapp.triphealing.databinding.FragmentProfileBinding
import com.healingapp.triphealing.secret.Secret
import com.healingapp.triphealing.viewmodel.post.NetworkViewModel
import com.healingapp.triphealing.viewmodel.user.UserViewModel

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModelPost: NetworkViewModel
    private lateinit var viewModelUser: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ProfileActivity에서 설정한 ViewModel 가져오기
        viewModelPost = ViewModelProvider(requireActivity())[NetworkViewModel::class.java]
        viewModelUser = ViewModelProvider(requireActivity())[UserViewModel::class.java]
        //viewModelUser = ViewModelProvider(this)[UserViewModel::class.java]

        viewModelUser.getNetworkUserResponseLiveData().observe(viewLifecycleOwner, Observer { response ->
            if (response != null){
                Log.e("TEST RESPONSE", response.toString())

                //로그인 되어 있을 시,
                if(response.code=="0000"){
                    Glide.with(this)
                        .load(Secret.MEDIA_URL+response.userInfo.profileImg)
                        .error(R.drawable.group_24)
                        .circleCrop()
                        .into(binding.imgProfile)

                    Glide.with(this)
                        .load(Secret.MEDIA_URL+response.userInfo.backgroundImg)
                        .error(R.drawable.background2)
                        .into(binding.imgProfileBack)

                    binding.tvNickname.text = response.userInfo.nickname
                    binding.tvIntroduce.text = response.userInfo.introduceText

                    //여행성향 예시
                    binding.tvPropensity.text = response.userInfo.propensity.option1

                    val data_list = ArrayList<String>()
                    data_list.add(response.userInfo.propensity.option1)
                    data_list.add(response.userInfo.propensity.option2)
                    data_list.add(response.userInfo.propensity.option3)
                    data_list.add(response.userInfo.propensity.option4)
                    data_list.add(response.userInfo.propensity.option5)

                    for(i:Int in 0 until data_list.size){

                    }
                }

                //로그인 되어 있지 않을 때
                else{
                    Glide.with(this)
                        .load(R.drawable.group_24)
                        .error(R.drawable.group_24)
                        .circleCrop()
                        .into(binding.imgProfile)

                    Glide.with(this)
                        .load(R.drawable.background2)
                        .error(R.drawable.background2)
                        .into(binding.imgProfileBack)

                    binding.tvNickname.text = "로그인해주세요"
                    binding.tvIntroduce.text = "로그인해주세요"

                    binding.tvFollower.isVisible = false
                    binding.tvFollowing.isVisible = false
                    binding.tvPropensity.isVisible = false

                }
            }
        })

    }

}