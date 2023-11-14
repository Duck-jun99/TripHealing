package com.healingapp.triphealing.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.healingapp.triphealing.datastore.DataStoreApplication
import com.healingapp.triphealing.databinding.FragmentUpdateBinding
import com.healingapp.triphealing.model.update.NetworkUpdateResponse
import com.healingapp.triphealing.model.user.NetworkUserResponse
import com.healingapp.triphealing.network.update.UpdateInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
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


        val data_list = mutableListOf<String>()

        // 데이터 리스트 업데이트 함수
        fun updateDataList(isChecked: Boolean, option: String) {
            if (isChecked) {
                if (data_list.size < 5) {
                    data_list.add(option)
                    binding.dataPropensity.text = data_list.toString()
                }
                else {
                    Toast.makeText(requireActivity(), "5개까지만 골라주세요.", Toast.LENGTH_SHORT).show()
                    when (option) {
                        binding.op1.text.toString() -> binding.op1.isChecked = false
                        binding.op2.text.toString() -> binding.op2.isChecked = false
                        binding.op3.text.toString() -> binding.op3.isChecked = false
                        binding.op4.text.toString() -> binding.op4.isChecked = false
                        binding.op5.text.toString() -> binding.op5.isChecked = false
                        binding.op6.text.toString() -> binding.op6.isChecked = false
                        binding.op7.text.toString() -> binding.op7.isChecked = false
                        binding.op8.text.toString() -> binding.op8.isChecked = false
                        binding.op9.text.toString() -> binding.op9.isChecked = false
                        binding.op10.text.toString() -> binding.op10.isChecked = false
                    }
                }
            }

            else {
                // 체크 해제된 경우 데이터 리스트에서 제거
                data_list.remove(option)

                binding.dataPropensity.text = data_list.toString()
            }
        }

        // 체크박스 상태 변경 리스너 설정
        binding.op1.setOnCheckedChangeListener { buttonView, isChecked ->
            updateDataList(isChecked, binding.op1.text.toString())
        }

        binding.op2.setOnCheckedChangeListener { buttonView, isChecked ->
            updateDataList(isChecked, binding.op2.text.toString())
        }

        binding.op3.setOnCheckedChangeListener { buttonView, isChecked ->
            updateDataList(isChecked, binding.op3.text.toString())
        }

        binding.op4.setOnCheckedChangeListener { buttonView, isChecked ->
            updateDataList(isChecked, binding.op4.text.toString())
        }

        binding.op5.setOnCheckedChangeListener { buttonView, isChecked ->
            updateDataList(isChecked, binding.op5.text.toString())
        }

        binding.op6.setOnCheckedChangeListener { buttonView, isChecked ->
            updateDataList(isChecked, binding.op6.text.toString())
        }

        binding.op7.setOnCheckedChangeListener { buttonView, isChecked ->
            updateDataList(isChecked, binding.op7.text.toString())
        }

        binding.op8.setOnCheckedChangeListener { buttonView, isChecked ->
            updateDataList(isChecked, binding.op8.text.toString())
        }

        binding.op9.setOnCheckedChangeListener { buttonView, isChecked ->
            updateDataList(isChecked, binding.op9.text.toString())
        }

        binding.op10.setOnCheckedChangeListener { buttonView, isChecked ->
            updateDataList(isChecked, binding.op10.text.toString())
        }




        binding.btnUpdate2.setOnClickListener {

            val nickname = binding.etNickname.text.toString()
            val email = binding.etEmail.text.toString()
            val introduce = binding.etIntroduce.text.toString()
            val profileImg = binding.etProfileImg.text.toString()
            val backgroundImg = binding.etBackgroundImg.text.toString()
            val mbti = binding.etMBTI.text.toString()

            //propensity부분 변경 필요
            val propensity =
                "{\"mbti\":\"${mbti}\"," +
                        "\"option1\":\"${data_list[0]}\"," +
                        "\"option2\":\"${data_list[1]}\"," +
                        "\"option3\":\"${data_list[2]}\"," +
                        "\"option4\":\"${data_list[3]}\"}"
                        //"\"option5\":\"${data_list[4]}\"}"

            Log.e("Propensity", propensity)

            scope.launch {
                val token = DataStoreApplication.getInstance().getDataStore().text.first()
                updateInterface.getNetwork(nickname = nickname,
                    useremail = email,
                    introduce_text = introduce,
                    propensity = propensity,
                    profile_img = profileImg,
                    background_img = backgroundImg,
                    token = token
                )
                    .enqueue(object : Callback<NetworkUpdateResponse> {
                    //서버 요청 성공
                    override fun onResponse(
                        call: Call<NetworkUpdateResponse>,
                        response: Response<NetworkUpdateResponse>
                    ) {
                        Log.e("Result: ", response.body().toString())
                        if (response.code().toString()=="0000"){
                            Toast.makeText(context,"회원정보 수정!",Toast.LENGTH_SHORT).show()
                        }

                    }

                    override fun onFailure(call: Call<NetworkUpdateResponse>, t: Throwable) {
                        //TODO("Not yet implemented")
                    }
                })
            }
        }

    }
}