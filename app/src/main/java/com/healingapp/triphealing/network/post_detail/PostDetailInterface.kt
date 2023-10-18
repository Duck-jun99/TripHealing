package com.healingapp.triphealing.network.post_detail

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.healingapp.triphealing.secret.Secret
import com.healingapp.triphealing.model.post.NetworkResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface PostDetailInterface {
    @FormUrlEncoded
    @POST(value = Secret.POST_DETAIL)
    fun getNetwork(
        @Field("id") id:String
    ):Call<NetworkResponse>

    companion object {
        fun create(): PostDetailInterface {
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
                .create(PostDetailInterface::class.java)
        }
    }

}