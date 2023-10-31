package com.healingapp.triphealing

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnticipateInterpolator
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging
import com.healingapp.triphealing.databinding.ActivityMainBinding
import com.healingapp.triphealing.datastore.DataStoreApplication
import com.healingapp.triphealing.secret.Secret
import com.healingapp.triphealing.utils.FCMService
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

    // 알림 카운트를 추적하기 위한 변수
    var notificationCount = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView하기 전에 installSplashScreen() 필수
        splashScreen = installSplashScreen()
        startSplash()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /** FCM설정, Token값 가져오기 */
        FCMService().getFirebaseToken()

        /** PostNotification 대응 */
        checkAppPushNotification()

        //사용안하면 삭제하기
        /** DynamicLink 수신확인 */
        initDynamicLink()

        //전체 메시지 구독용
        subscribeTopics()

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




        //[ 여기부터 수정 필요
        fun updateBottomNavigationBarIcon(){
            var menuItem = binding.navBar.menu.findItem(R.id.writeFragment)

            if (notificationCount > 0) {
                // 알림이 있는 경우, 알림 카운트 표시
                menuItem.setIcon(R.drawable.baseline_notifications_active_24)
            } else {
                // 알림이 없는 경우, 일반 아이콘으로 표시
                menuItem.setIcon(R.drawable.baseline_notifications_24)
            }
        }

        // 알림이 도착할 때 알림 카운트 증가
        fun onNewNotificationArrived() {
            notificationCount++
            updateBottomNavigationBarIcon()
        }

        // 알림을 확인하고 카운트를 초기화하는 함수
        fun clearNotificationCount() {
            notificationCount = 0
            updateBottomNavigationBarIcon()
        }

        // 여기까지 수정필요 ]




    }

    // splash의 애니메이션 설정
    private fun startSplash() {
        splashScreen.setOnExitAnimationListener { splashScreenView ->
            val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1f, 5f, 1f)
            val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, 5f, 1f)

            try {
                ObjectAnimator.ofPropertyValuesHolder(splashScreenView.iconView, scaleX, scaleY).run {
                    interpolator = AnticipateInterpolator()
                    duration = 1000L
                    doOnEnd {
                        splashScreenView.remove()
                    }
                    start()
                }
            } catch (e: Error) {
                // 오류가 발생한 경우 토스트 메시지로 에러 출력
                Log.e("SplashScreen", "에러: ${e.message}")
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

    /** Android 13 PostNotification */
    private fun checkAppPushNotification() {
        //Android 13 이상 && 푸시권한 없음
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
            && PackageManager.PERMISSION_DENIED == ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)) {
            // 푸쉬 권한 없음
            permissionPostNotification.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            return
        }

        //권한이 있을때
        //TODO
    }

    /** 권한 요청 */
    private val permissionPostNotification = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            //권한 허용
        } else {
            //권한 비허용
        }
    }


    //사용안하면 삭제하기
    /** DynamicLink */
    private fun initDynamicLink() {
        val dynamicLinkData = intent.extras
        if (dynamicLinkData != null) {
            var dataStr = "DynamicLink 수신받은 값\n"
            for (key in dynamicLinkData.keySet()) {
                dataStr += "key: $key / value: ${dynamicLinkData.getString(key)}\n"
            }
            //binding.tvToken.text = dataStr
        }
    }

    //전체 공지 구독용
    fun subscribeTopics() {
        //
        Firebase.messaging.subscribeToTopic(Secret.NOTI_ALL)
            .addOnCompleteListener { task ->
                var msg = "Subscribed"
                if (!task.isSuccessful) {
                    msg = "Subscribe failed"
                }
                Log.d("Firebase", msg)
            }
    }



}