
package com.healingapp.triphealing.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.healingapp.triphealing.R
import com.healingapp.triphealing.databinding.FragmentSignUpBinding
import com.healingapp.triphealing.network.signup.SignUpInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    //private lateinit var viewModelUser: UserViewModel

    private val scope = CoroutineScope(Dispatchers.Default)

    val signUpInterface by lazy { SignUpInterface.create() }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.btnJoinNext.setOnClickListener {


            var joinID:String = binding.etJoinID.text.toString()
            var joinPW1:String = binding.etJoinPW.text.toString()
            var joinPW2:String = binding.etJoinPW2.text.toString()
            var joinEmail:String = binding.etJoinEmail.text.toString()



            if(joinPW1 != joinPW2){
                Toast.makeText(requireActivity(),"비밀번호를 다시 확인해주세요.",Toast.LENGTH_SHORT).show()
            }
            else{
                var joinData1 = ArrayList<String>()
                joinData1.add(joinID)
                joinData1.add(joinPW1)
                joinData1.add(joinPW2)
                joinData1.add(joinEmail)
                setFragmentResult("requestKey", bundleOf("bundleKey" to joinData1))
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_view_signup, SignUpFragment2())
                    .commit()
            }

        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}

