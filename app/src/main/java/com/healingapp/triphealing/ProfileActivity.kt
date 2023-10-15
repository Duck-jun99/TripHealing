package com.healingapp.triphealing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.healingapp.triphealing.databinding.ActivityProfileBinding
import com.healingapp.triphealing.viewmodel.post.NetworkViewModel
import com.healingapp.triphealing.viewmodel.user.UserViewModel

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    private lateinit var viewModelPost: NetworkViewModel
    private lateinit var viewModelUser: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getStringExtra("id")
        val pw = intent.getStringExtra("pw")

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