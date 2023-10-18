package com.healingapp.triphealing.network.signup

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.healingapp.triphealing.secret.Secret
import com.healingapp.triphealing.model.signup.NetworkSignUpResponse
import com.healingapp.triphealing.model.user.Propensity
import com.healingapp.triphealing.model.user.SignUpData
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface SignUpInterface {
    @FormUrlEncoded
    @JvmSuppressWildcards
    @POST(value = Secret.SIGNUP_VALUE)
    @Headers(
        "accept: application/json"
    )
    fun getNetwork(
        @Field("username") username:String,
        @Field("password1") password1:String,
        @Field("password2") password2:String,
        @Field("email") email:String,
        @Field("nickname") nickname:String,
        @Field("introduce_text") introduceText:String,
        @Field("profile_img") profileImg:String,
        @Field("background_img") backgroudImg:String,
        @Field("propensity") propensity:String,
    ):Call<NetworkSignUpResponse>

    companion object {
        fun create(): SignUpInterface {
            val gson: Gson = GsonBuilder().setLenient().create()

            val interceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

            return Retrofit.Builder()
                .baseUrl(Secret.USER_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(SignUpInterface::class.java)
        }
    }
}