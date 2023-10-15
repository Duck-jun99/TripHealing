package com.healingapp.triphealing.network.post

import android.util.Log
import androidx.core.content.res.TypedArrayUtils.getString
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.healingapp.triphealing.R
import com.healingapp.triphealing.Secret
import com.healingapp.triphealing.model.post.NetworkResponse
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.logging.HttpLoggingInterceptor

class NetworkRepository {

    companion object {
        private const val TAG = "NetworkRepository"
        private const val BASE_URL = Secret.HOME_URL
    }

    private lateinit var networkInterface: NetworkInterface
    private var networkMutableLiveData: MutableLiveData<List<NetworkResponse>> = MutableLiveData()
    private var isInitialized = false

    init {
        initializeNetworkInterface()
    }

    private fun initializeNetworkInterface() {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client: OkHttpClient = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()

            val gson: Gson = GsonBuilder().setLenient().create()

            networkInterface = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                //.addConverterFactory(ScalarsConverterFactory.create())
                .build()
                .create(NetworkInterface::class.java)

            isInitialized = true // 초기화가 완료되면 플래그를 설정합니다.

    }

    fun getNetwork() {

        if (!isInitialized) {
            // 초기화가 완료되지 않았다면 대기합니다.
            return
        }

        networkInterface.getNetwork().enqueue(object : Callback<List<NetworkResponse>?> {
            override fun onResponse(call: Call<List<NetworkResponse>?>, response: Response<List<NetworkResponse>?>) {
                //Log.d(TAG, "onResponse: ${GsonBuilder().setPrettyPrinting().create().toJson(response.body())}")
                networkMutableLiveData.postValue(response.body())
            }

            override fun onFailure(call: Call<List<NetworkResponse>?>, t: Throwable) {
                Log.e(TAG, "onFailure: error. cause: ${t.message}")
                networkMutableLiveData.postValue(null)
            }
        })



    }

    fun getNetworkResponseLiveData(): LiveData<List<NetworkResponse>> {
        return this.networkMutableLiveData
    }
}
