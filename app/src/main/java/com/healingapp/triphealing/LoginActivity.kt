package com.healingapp.triphealing

import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.healingapp.triphealing.databinding.ActivityLoginBinding
import com.healingapp.triphealing.datastore.DataStoreApplication
import com.healingapp.triphealing.viewmodel.user.UserChangeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var userViewModel: UserChangeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        userViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application))[UserChangeViewModel::class.java]

        binding.etPW.transformationMethod=PasswordTransformationMethod.getInstance()


        binding.loginbtn.setOnClickListener {
            binding.loginbtn.isVisible = false
            binding.linearLayout.isVisible = true
        }

        binding.loginbtn2.setOnClickListener{

            var id:String = binding.etID.text.toString()
            var pw:String = binding.etPW.text.toString()


            //조건식 수정 필요
            if(binding.etID == null || binding.etID.equals("") || binding.etID.length() == 0
                || binding.etPW == null || binding.etPW.equals("") || binding.etPW.length() == 0 )
            {
                Toast.makeText(
                    this,
                    "ID, PW를 입력해주세요.",
                    Toast.LENGTH_SHORT
                ).show()
            }

            else{

                if (::userViewModel.isInitialized){
                    userViewModel.getNetwork(id,pw)
                }

                userViewModel.getNetworkUserResponseLiveData().observe(this, Observer
                { response ->
                    if(response != null){
                        Log.e("TEST LOGIN", response.toString())

                        if(response.code == "0000"){

                            CoroutineScope(Dispatchers.Main).launch {
                                DataStoreApplication.getInstance().getDataStore().setToken(response.token)
                                DataStoreApplication.getInstance().getDataStore().setUser(response.userInfo.username)
                            }


                            val intent = Intent(this, MainActivity::class.java)

                            startActivity(intent)
                            finish()
                        }
                        else{
                            Toast.makeText(this,"ID 또는 PW가 틀렸습니다.", Toast.LENGTH_SHORT).show()
                        }


                    }
                    else{
                        Toast.makeText(this,"서버와 통신에 실패했습니다.", Toast.LENGTH_SHORT).show()
                    }
                })

            }

        }

        binding.joinbtn.setOnClickListener {
            //binding.framelayoutJoin.isVisible = true
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
    }

}