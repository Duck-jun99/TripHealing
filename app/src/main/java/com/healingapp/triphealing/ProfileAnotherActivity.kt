package com.healingapp.triphealing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.healingapp.triphealing.databinding.ActivityProfileAnotherBinding
import com.healingapp.triphealing.databinding.ActivityProfileBinding
import com.healingapp.triphealing.datastore.DataStoreApplication
import com.healingapp.triphealing.view.ProfileFragment
import com.healingapp.triphealing.view.UpdateFragment
import com.healingapp.triphealing.viewmodel.post_all.NetworkViewModel
import com.healingapp.triphealing.viewmodel.user.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class ProfileAnotherActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileAnotherBinding

    lateinit var userName:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileAnotherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //token = intent.getStringExtra("token").toString()
        userName = intent.getStringExtra("username").toString()
        Log.e("ProfileAnotherActivity",userName)

    }

}