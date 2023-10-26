package com.healingapp.triphealing

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AnticipateInterpolator
import android.widget.Toast
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.healingapp.triphealing.databinding.ActivityMainBinding
import com.healingapp.triphealing.datastore.DataStoreApplication
import com.healingapp.triphealing.view.MainFragment
import com.healingapp.triphealing.viewmodel.post_all.NetworkViewModel
import com.healingapp.triphealing.viewmodel.user.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var splashScreen: SplashScreen

    private lateinit var binding: ActivityMainBinding
    //lateinit var navController: NavController

    private lateinit var viewModelPost: NetworkViewModel
    private lateinit var viewModelUser: UserViewModel

    lateinit var id:String
    lateinit var pw:String
    lateinit var token:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView하기 전에 installSplashScreen() 필수
        splashScreen = installSplashScreen()
        startSplash()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModelPost = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application))[NetworkViewModel::class.java]
        viewModelUser = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application))[UserViewModel::class.java]



        id = intent.getStringExtra("id").toString() // LoginActivity에서 전달된 데이터 읽기
        pw = intent.getStringExtra("pw").toString()
        //token = intent.getStringExtra("token").toString()

        CoroutineScope(Dispatchers.Main).launch {
            token = DataStoreApplication.getInstance().getDataStore().text.first()
            if (::viewModelUser.isInitialized){
                if (id != null && pw != null) {
                    viewModelUser.getNetwork(token)
                }
            }
            else{
                Toast.makeText(applicationContext, "Server Error", Toast.LENGTH_SHORT).show()
            }
        }


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

    // splash의 애니메이션 설정
    private fun startSplash() {
        splashScreen.setOnExitAnimationListener { splashScreenView ->
            val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1f, 5f, 1f)
            val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, 5f, 1f)

            ObjectAnimator.ofPropertyValuesHolder(splashScreenView.iconView, scaleX, scaleY).run {
                interpolator = AnticipateInterpolator()
                duration = 1000L
                doOnEnd {
                    splashScreenView.remove()
                }
                start()
            }
        }
    }

    fun initPostListeners() {
        if (::viewModelPost.isInitialized){
            viewModelPost.getNetwork()
        }
        else Toast.makeText(this, "Server Error", Toast.LENGTH_SHORT).show()
    }



    fun initUserListeners(token: String){
        if (::viewModelUser.isInitialized){
            viewModelUser.getNetwork(token)
        }
        else Toast.makeText(this, "Server Error", Toast.LENGTH_SHORT).show()

    }

}