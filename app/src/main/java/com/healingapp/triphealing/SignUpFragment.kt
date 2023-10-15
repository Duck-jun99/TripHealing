
package com.healingapp.triphealing

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.findFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.google.gson.JsonArray
import com.healingapp.triphealing.databinding.FragmentJoinBinding
import com.healingapp.triphealing.model.signup.NetworkSignUpResponse
import com.healingapp.triphealing.model.user.NetworkUserResponse
import com.healingapp.triphealing.network.signup.SignUpInterface
import com.healingapp.triphealing.viewmodel.user.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpFragment : Fragment() {

    private var _binding: FragmentJoinBinding? = null
    private val binding get() = _binding!!

    //private lateinit var viewModelUser: UserViewModel

    private val scope = CoroutineScope(Dispatchers.Default)

    val signUpInterface by lazy { SignUpInterface.create() }




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentJoinBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.btnJoin.setOnClickListener {

            var joinID:String = binding.etJoinID.text.toString()
            var joinPW1:String = binding.etJoinPW.text.toString()
            var joinPW2:String = binding.etJoinPW.text.toString()
            var joinEmail:String = binding.etJoinEmail.text.toString()

            val joinPropensity = listOf(
                mapOf("option1" to "enfp", "option2" to "hi", "option3" to "", "option4" to "", "option5" to ""))

            Log.e("joinID", joinID)
            Log.e("joinPW1", joinPW1)
            Log.e("joinPW2", joinPW2)
            Log.e("joinEmail", joinEmail)
            scope.launch {

                signUpInterface.getNetwork(joinID,joinPW1,joinPW2,joinEmail,joinPropensity).enqueue(object : Callback<NetworkSignUpResponse>
                {
                    //서버 요청 성공
                    override fun onResponse(call: Call<NetworkSignUpResponse>, response: Response<NetworkSignUpResponse>) {
                        Log.e("Successful Message: ", "데이터 성공적으로 수신")
                        Log.e("Result: ", response.body().toString())

                        if (response.body() != null){
                            if (response.body()!!.code == "0000"){
                                Toast.makeText(activity,"회원가입 성공입니다.",Toast.LENGTH_SHORT).show()
                                requireActivity().supportFragmentManager.beginTransaction().remove(binding.root.findFragment()).commit()
                            }
                            else {
                                Toast.makeText(activity,"회원가입 실패입니다.",Toast.LENGTH_SHORT).show()
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

    override fun onDestroyView() {
        super.onDestroyView()
    }
}

