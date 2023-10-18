package com.healingapp.triphealing.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.findFragment
import androidx.fragment.app.setFragmentResultListener
import com.healingapp.triphealing.LoginActivity
import com.healingapp.triphealing.databinding.FragmentSignUp2Binding
import com.healingapp.triphealing.model.signup.NetworkSignUpResponse
import com.healingapp.triphealing.network.signup.SignUpInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpFragment2 : Fragment() {
    private var _binding: FragmentSignUp2Binding? = null
    private val binding get() = _binding!!

    private val scope = CoroutineScope(Dispatchers.Default)

    val signUpInterface by lazy { SignUpInterface.create() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignUp2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setFragmentResultListener("requestKey") { requestKey, bundle ->
            var joinData1 = bundle.getStringArrayList("bundleKey")!!
            //Log.e("TEST joinData1", joinData1.toString())

            var joinID = joinData1[0]
            var joinPW1 = joinData1[1]
            var joinPW2 = joinData1[2]
            var joinEmail = joinData1[3]

            val data_list = mutableListOf<String>()

            // 데이터 리스트 업데이트 함수
            fun updateDataList(isChecked: Boolean, option: String) {
                if (isChecked) {
                    if (data_list.size < 4) {
                        data_list.add(option)
                    }
                    else {
                        Toast.makeText(requireActivity(), "4개까지만 골라주세요.", Toast.LENGTH_SHORT).show()
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

            binding.btnJoin.setOnClickListener {

                val joinNickname = binding.etJoinNickname.text.toString()
                val joinIntroduce = binding.etJoinIntroduce.text.toString()
                val joinMBTI = binding.etjoinMBTI.text.toString()
                val propensity =
                "{\"mbti\":\"${joinMBTI}\"," +
                        "\"option1\":\"${data_list[0]}\"," +
                        "\"option2\":\"${data_list[1]}\"," +
                        "\"option3\":\"${data_list[2]}\"," +
                        "\"option4\":\"${data_list[3]}\"}"
                //"\"option5\":\"${data_list[4]}\"}"

                Log.e("Propensity", propensity)

                scope.launch {


                    signUpInterface.getNetwork(username = joinID,
                        password1 = joinPW1,
                        password2 = joinPW2,
                        email = joinEmail,
                        nickname = joinNickname,
                        introduceText = joinIntroduce,
                        profileImg = "",
                        backgroudImg = "",
                        propensity = propensity)
                        .enqueue(object : Callback<NetworkSignUpResponse>
                    {
                        //서버 요청 성공
                        override fun onResponse(call: Call<NetworkSignUpResponse>, response: Response<NetworkSignUpResponse>) {
                            Log.e("Successful Message: ", "데이터 성공적으로 수신")
                            Log.e("Result: ", response.body().toString())

                            if (response.body() != null){
                                if (response.body()!!.code == "0000"){
                                    Toast.makeText(activity,"회원가입 성공입니다.", Toast.LENGTH_SHORT).show()
                                    requireActivity().supportFragmentManager.beginTransaction().remove(binding.root.findFragment()).commit()
                                    val intent = Intent(context, LoginActivity::class.java)
                                    startActivity(intent)
                                }
                                else {
                                    Toast.makeText(activity,"회원가입 실패입니다.", Toast.LENGTH_SHORT).show()
                                }
                            }

                        }
                        //서버 요청 실패
                        override fun onFailure(call: Call<NetworkSignUpResponse>, t: Throwable)
                        {
                            Log.e("Error Message : ",  t.message.toString())
                        }
                    })




                }
            }

        }






    }

}