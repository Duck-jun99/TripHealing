package com.healingapp.triphealing.network.post_mbti

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.healingapp.triphealing.model.post.NetworkRecWriterResponse
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

interface PostMBTIInterface {
    @FormUrlEncoded
    @POST(value = Secret.POST_MBTI)

    fun getNetwork(
        @Field("mbti") mbti:String
    ):Call<List<NetworkRecWriterResponse>>

    companion object {
        fun create(): PostMBTIInterface {
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
                .create(PostMBTIInterface::class.java)
        }
    }

}