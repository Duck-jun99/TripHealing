package com.healingapp.triphealing

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.healingapp.triphealing.databinding.FragmentMainBinding
import com.healingapp.triphealing.databinding.FragmentUpdateBinding
import com.healingapp.triphealing.model.signup.NetworkSignUpResponse
import com.healingapp.triphealing.model.update.NetworkUpdateResponse
import com.healingapp.triphealing.network.signup.SignUpInterface
import com.healingapp.triphealing.network.update.UpdateInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateFragment : Fragment() {

    private var _binding: FragmentUpdateBinding? = null
    private val binding get() = _binding!!

    private val scope = CoroutineScope(Dispatchers.Default)

    val updateInterface by lazy { UpdateInterface.create() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUpdateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnUpdate2.setOnClickListener {

            val nickname = binding.etNickname.text.toString()
            val email = binding.etEmail.text.toString()
            val introduce = binding.etIntroduce.text.toString()
            val propensity = binding.etPropensity.text.toString()
            val profileImg = binding.etProfileImg.text.toString()
            val backgroundImg = binding.etBackgroundImg.text.toString()

            scope.launch {
                updateInterface.getNetwork(nickname = nickname, useremail = email,
                    introduce_text = introduce, propensity = propensity,
                    profile_img = profileImg, background_img = backgroundImg)
                    .enqueue(object : Callback<NetworkUpdateResponse> {
                    //서버 요청 성공
                    override fun onResponse(
                        call: Call<NetworkUpdateResponse>,
                        response: Response<NetworkUpdateResponse>
                    ) {
                        Log.e("Successful Message: ", "데이터 성공적으로 수신")
                        Log.e("Result: ", response.body().toString())

                    }

                    override fun onFailure(call: Call<NetworkUpdateResponse>, t: Throwable) {
                        TODO("Not yet implemented")
                    }
                })
            }
        }

    }
}