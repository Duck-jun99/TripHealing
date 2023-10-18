package com.healingapp.triphealing.network.write

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.healingapp.triphealing.secret.Secret
import com.healingapp.triphealing.model.user.NetworkUserResponse
import com.healingapp.triphealing.model.write.NetworkWriteResponse
import com.healingapp.triphealing.network.signup.SignUpInterface
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface WriteInterface {
    @FormUrlEncoded
    @POST(value = Secret.WRITE_URL)
    fun getNetwork(
        @Header("Authorization") token:String,
        @Field("title") title:String,
        @Field("description") description:String,
        @Field("created_date") createdDate:String,
        @Field("cover_img") coverImg:String,
    ):Call<NetworkWriteResponse>

    companion object {
        fun create(): WriteInterface {
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
                .create(WriteInterface::class.java)
        }
    }
}