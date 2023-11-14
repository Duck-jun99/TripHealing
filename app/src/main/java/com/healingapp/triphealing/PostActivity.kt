package com.healingapp.triphealing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.healingapp.triphealing.datastore.DataStoreApplication
import com.healingapp.triphealing.viewmodel.post_all.NetworkViewModel
import com.healingapp.triphealing.viewmodel.user.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class PostActivity : AppCompatActivity() {

    //private lateinit var viewModelPost: NetworkViewModel
    private lateinit var viewModelUser: UserViewModel

    lateinit var id:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        id = intent.getStringExtra("id").toString()

        CoroutineScope(Dispatchers.Main).launch {
            val token = DataStoreApplication.getInstance().getDataStore().text.first()
            if (::viewModelUser.isInitialized){

                viewModelUser.getNetwork(token)
            }
            else{
                Toast.makeText(applicationContext, "Server Error", Toast.LENGTH_SHORT).show()
            }

        }

        //viewModelPost = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application))[NetworkViewModel::class.java]
        viewModelUser = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application))[UserViewModel::class.java]


    }


}