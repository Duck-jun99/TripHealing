package com.healingapp.triphealing

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import coil.load
import coil.transform.CircleCropTransformation
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.healingapp.triphealing.databinding.ActivityMainBinding
import com.healingapp.triphealing.view.MainFragment
import com.healingapp.triphealing.viewmodel.post.NetworkViewModel
import com.healingapp.triphealing.viewmodel.user.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    //lateinit var navController: NavController

    private lateinit var viewModelPost: NetworkViewModel
    private lateinit var viewModelUser: UserViewModel

    lateinit var id:String
    lateinit var pw:String
    //lateinit var token:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        id = intent.getStringExtra("id").toString() // LoginActivity에서 전달된 데이터 읽기
        pw = intent.getStringExtra("pw").toString()
        //token = intent.getStringExtra("token").toString()

        CoroutineScope(Dispatchers.Main).launch {
            val token = DataStoreApplication.getInstance().getDataStore().text.first()
            if (::viewModelUser.isInitialized){
                if (id != null && pw != null) {
                    viewModelUser.getNetwork(token)
                }
            }
            else{
                Toast.makeText(applicationContext, "Server Error", Toast.LENGTH_SHORT).show()
            }
        }


        viewModelPost = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application))[NetworkViewModel::class.java]
        viewModelUser = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application))[UserViewModel::class.java]

        if (::viewModelPost.isInitialized){
            viewModelPost.getNetwork()
        }
        else{
            Toast.makeText(this, "Server Error", Toast.LENGTH_SHORT).show()
        }


        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        val navController = navHostFragment.navController

        binding.navBar.setupWithNavController(navController)

    }
}