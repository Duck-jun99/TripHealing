
package com.healingapp.triphealing.network.user_info

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.healingapp.triphealing.model.post.NetworkRecWriterResponse
import com.healingapp.triphealing.secret.Secret
import com.healingapp.triphealing.model.post.NetworkResponse
import com.healingapp.triphealing.model.user.UserInfoResponse
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

interface UserInfoInterface {
    @FormUrlEncoded
    @POST(value = Secret.USER_INFO_URL)

    fun getNetwork(
        @Field("username") userName:String
    ):Call<UserInfoResponse>

    @FormUrlEncoded
    @POST(value = Secret.USER_DETATIL_URL)

    fun postFollow(
        @Field("username") userName:String,
        @Field("user_me_name") userMeName:String,
        @Field("follow") follow:String
    ):Call<UserInfoResponse>

    companion object {
        fun create(): UserInfoInterface {
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
                .create(UserInfoInterface::class.java)
        }
    }

}
