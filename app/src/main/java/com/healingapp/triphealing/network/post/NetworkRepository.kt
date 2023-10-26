package com.healingapp.triphealing.network.post

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.healingapp.triphealing.model.post.NetworkRecWriterResponse
import com.healingapp.triphealing.secret.Secret
import com.healingapp.triphealing.model.post.NetworkResponse
import kotlinx.coroutines.currentCoroutineContext
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.logging.HttpLoggingInterceptor
import kotlin.coroutines.coroutineContext

class NetworkRepository {

    companion object {
        private const val TAG = "NetworkRepository"
        private const val BASE_URL = Secret.HOME_URL
    }

    //post All
    private lateinit var networkInterface: NetworkInterface
    private var networkMutableLiveData: MutableLiveData<List<NetworkResponse>> = MutableLiveData()

    //post Famous
    private lateinit var postFamousInterface: PostFamousInterface
    private var postFamousMutableLiveData: MutableLiveData<List<NetworkResponse>> = MutableLiveData()

    //post Recommendation
    private lateinit var postRecInterface: PostRecInterface
    private var postRecMutableLiveData: MutableLiveData<List<NetworkResponse>> = MutableLiveData()

    //post Recommended writer
    private lateinit var postRecWriterInterface: PostRecWriterInterface
    private var postRecWriterMutableLiveData: MutableLiveData<List<NetworkRecWriterResponse>> = MutableLiveData()


    private var isInitialized = false

    init {
        initializeNetworkInterface()
    }

    //initialize post All, post Famous, post Recommendation, post Recommended writer
    private fun initializeNetworkInterface() {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client: OkHttpClient = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()

            val gson: Gson = GsonBuilder().setLenient().create()

            //post all
            networkInterface = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                //.addConverterFactory(ScalarsConverterFactory.create())
                .build()
                .create(NetworkInterface::class.java)

            //post famous
            postFamousInterface = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                //.addConverterFactory(ScalarsConverterFactory.create())
                .build()
                .create(PostFamousInterface::class.java)

            //post Recommendation
            postRecInterface = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                //.addConverterFactory(ScalarsConverterFactory.create())
                .build()
                .create(PostRecInterface::class.java)

            //post Recommended writer
            postRecWriterInterface = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                //.addConverterFactory(ScalarsConverterFactory.create())
                .build()
                .create(PostRecWriterInterface::class.java)

            isInitialized = true // 초기화가 완료되면 플래그를 설정합니다.

    }

    //getNetwork post all, post famous, post recommendation, post Recommended writer
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



        postFamousInterface.getNetwork().enqueue(object : Callback<List<NetworkResponse>?> {
            override fun onResponse(call: Call<List<NetworkResponse>?>, response: Response<List<NetworkResponse>?>) {
                //Log.d(TAG, "onResponse: ${GsonBuilder().setPrettyPrinting().create().toJson(response.body())}")
                postFamousMutableLiveData.postValue(response.body())
            }

            override fun onFailure(call: Call<List<NetworkResponse>?>, t: Throwable) {
                Log.e(TAG, "onFailure: error. cause: ${t.message}")
                postFamousMutableLiveData.postValue(null)
            }
        })

        postRecInterface.getNetwork().enqueue(object : Callback<List<NetworkResponse>?> {
            override fun onResponse(call: Call<List<NetworkResponse>?>, response: Response<List<NetworkResponse>?>) {
                //Log.d(TAG, "onResponse: ${GsonBuilder().setPrettyPrinting().create().toJson(response.body())}")
                postRecMutableLiveData.postValue(response.body())
            }

            override fun onFailure(call: Call<List<NetworkResponse>?>, t: Throwable) {
                Log.e(TAG, "onFailure: error. cause: ${t.message}")
                postRecMutableLiveData.postValue(null)
            }
        })


        postRecWriterInterface.getNetwork().enqueue(object : Callback<List<NetworkRecWriterResponse>?> {
            override fun onResponse(call: Call<List<NetworkRecWriterResponse>?>, response: Response<List<NetworkRecWriterResponse>?>) {
                //Log.d(TAG, "onResponse: ${GsonBuilder().setPrettyPrinting().create().toJson(response.body())}")
                postRecWriterMutableLiveData.postValue(response.body())
            }

            override fun onFailure(call: Call<List<NetworkRecWriterResponse>?>, t: Throwable) {
                Log.e(TAG, "onFailure: error. cause: ${t.message}")
                postRecWriterMutableLiveData.postValue(null)
            }
        })



    }

    //post all
    fun getNetworkResponseLiveData(): LiveData<List<NetworkResponse>> {
        return this.networkMutableLiveData
    }

    //post famous
    fun getPostFamousLiveData(): LiveData<List<NetworkResponse>> {
        return this.postFamousMutableLiveData
    }

    //post recommendation
    fun getPostRecLiveData(): LiveData<List<NetworkResponse>> {
        return this.postRecMutableLiveData
    }

    //post Recommended writer
    fun getPostRecWriterLiveData(): LiveData<List<NetworkRecWriterResponse>> {
        return this.postRecWriterMutableLiveData
    }
}
