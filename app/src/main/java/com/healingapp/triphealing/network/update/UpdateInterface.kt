package com.healingapp.triphealing.network.update

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.healingapp.triphealing.secret.Secret
import com.healingapp.triphealing.model.update.NetworkUpdateResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface UpdateInterface {
    @FormUrlEncoded
    @JvmSuppressWildcards
    @POST(value = Secret.UPDATE_VALUE)
    @Headers(
        "accept: application/json",
        "content-type: application/x-www-form-urlencoded; charset=utf-8"
    )
    fun getNetwork(
        @Field("useremail") useremail:String,
        //@Field("password") password:String,
        @Field("nickname") nickname:String,
        @Field("introduce_text") introduce_text:String,
        @Field("profile_img") profile_img:String,
        @Field("background_img") background_img:String,
        @Field("propensity") propensity:String,
    ):Call<NetworkUpdateResponse>

    companion object {
        fun create(): UpdateInterface {
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
                .create(UpdateInterface::class.java)
        }
    }
}