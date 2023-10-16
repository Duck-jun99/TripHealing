package com.healingapp.triphealing.network.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.healingapp.triphealing.secret.Secret
import com.healingapp.triphealing.model.user.NetworkUserResponse
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.logging.HttpLoggingInterceptor

class UserChangeRepository {

    companion object {
        private const val TAG = "UserRepository"
        private const val USER_URL = Secret.USER_URL

    }

    private lateinit var userInterface: UserChangeInterface
    private var userMutableLiveData: MutableLiveData<NetworkUserResponse> = MutableLiveData()
    private var isInitialized = false

    init {
        initializeUserInterface()
    }

    private fun initializeUserInterface() {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client: OkHttpClient = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()

            val gson: Gson = GsonBuilder().setLenient().create()

            userInterface = Retrofit.Builder()
                .baseUrl(USER_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                //.addConverterFactory(ScalarsConverterFactory.create())
                .build()
                .create(UserChangeInterface::class.java)

            isInitialized = true // 초기화가 완료되면 플래그를 설정합니다.

    }

    fun getNetwork(username: String, password: String) {

        if (!isInitialized) {
            // 초기화가 완료되지 않았다면 대기합니다.
            return
        }

        userInterface.getNetwork(username, password).enqueue(object : Callback<NetworkUserResponse> {
            override fun onResponse(call: Call<NetworkUserResponse>, response: Response<NetworkUserResponse>) {
                //Log.d(TAG, "onResponse: ${GsonBuilder().setPrettyPrinting().create().toJson(response.body())}")
                userMutableLiveData.postValue(response.body())
            }

            override fun onFailure(call: Call<NetworkUserResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: error. cause: ${t.message}")
                userMutableLiveData.postValue(null)
            }
        })



    }

    fun getNetworkUserResponseLiveData(): LiveData<NetworkUserResponse> {
        return this.userMutableLiveData
    }
}
