package com.healingapp.triphealing.network.login

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.healingapp.triphealing.model.post.NetworkResponse
import com.healingapp.triphealing.secret.Secret
import com.healingapp.triphealing.model.user.NetworkUserResponse
import com.healingapp.triphealing.network.post_detail.PostDetailInterface
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface UserInterface {

    @POST(value = Secret.LOGIN_VALUE)
    fun getNetwork(
        @Header("Authorization") token:String,
    ):Call<NetworkUserResponse>


}

interface UserPostInterface{
    @FormUrlEncoded
    @POST(value = Secret.USER_POST_URL)
    fun getNetwork(
        @Field("username") username:String
    ):Call<List<NetworkResponse>>

    companion object {
        fun create(): UserPostInterface {
            val gson: Gson = GsonBuilder().setLenient().create()

            val interceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

            return Retrofit.Builder()
                .baseUrl(Secret.WRITE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(UserPostInterface::class.java)
        }
    }
}