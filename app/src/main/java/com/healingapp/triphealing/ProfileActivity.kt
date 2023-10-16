package com.healingapp.triphealing

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.service.voice.VoiceInteractionSession.ActivityId
import android.util.Log
import androidx.activity.viewModels
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.healingapp.triphealing.databinding.ActivityProfileBinding
import com.healingapp.triphealing.view.ProfileFragment
import com.healingapp.triphealing.view.UpdateFragment
import com.healingapp.triphealing.viewmodel.post.NetworkViewModel
import com.healingapp.triphealing.viewmodel.user.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking



class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    private lateinit var viewModelPost: NetworkViewModel
    private lateinit var viewModelUser: UserViewModel


    lateinit var id: String
    lateinit var pw: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        id = intent.getStringExtra("id").toString()
        pw = intent.getStringExtra("pw").toString()
        //token = intent.getStringExtra("token").toString()


        //ViewModel 가져오기
        viewModelPost = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application))[NetworkViewModel::class.java]
        viewModelUser = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application))[UserViewModel::class.java]

        //viewModelDataStore = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application))[DataStoreViewModel::class.java]



        CoroutineScope(Dispatchers.Main).launch {
            val token = DataStoreApplication.getInstance().getDataStore().text.first()
            Log.e("TEST TOKEN", DataStoreApplication.getInstance().getDataStore().text.first())

            if (id != null && pw != null) {
                viewModelUser.getNetwork(token)
            }
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