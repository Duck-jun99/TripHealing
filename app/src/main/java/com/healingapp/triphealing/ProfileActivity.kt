package com.healingapp.triphealing

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.service.voice.VoiceInteractionSession.ActivityId
import androidx.lifecycle.ViewModelProvider
import com.healingapp.triphealing.databinding.ActivityProfileBinding
import com.healingapp.triphealing.view.ProfileFragment
import com.healingapp.triphealing.view.UpdateFragment
import com.healingapp.triphealing.viewmodel.post.NetworkViewModel
import com.healingapp.triphealing.viewmodel.user.UserViewModel

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    private lateinit var viewModelPost: NetworkViewModel
    private lateinit var viewModelUser: UserViewModel

    lateinit var id: String
    lateinit var pw: String
    lateinit var token: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        id = intent.getStringExtra("id").toString()
        pw = intent.getStringExtra("pw").toString()
        token = intent.getStringExtra("token").toString()

        //ViewModel 가져오기
        viewModelPost = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application))[NetworkViewModel::class.java]
        viewModelUser = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application))[UserViewModel::class.java]


        if (id != null && pw != null) {
            viewModelUser.getNetwork(id,pw)
        }

        viewModelPost.getNetwork()


        binding.btnUpdate.setOnClickListener {

            val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container_view_profile)
            if (fragment is ProfileFragment) {

                val fragment_update = supportFragmentManager.beginTransaction()
                fragment_update.replace(
                    R.id.fragment_container_view_profile,
                    UpdateFragment()
                )
                fragment_update.commit()
                //MainFragment 생성 코드 종료
            }

        }

    }

}