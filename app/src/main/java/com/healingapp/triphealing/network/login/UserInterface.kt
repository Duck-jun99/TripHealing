package com.healingapp.triphealing.network.login

import com.healingapp.triphealing.Secret
import com.healingapp.triphealing.model.user.NetworkUserResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface UserInterface {
    @FormUrlEncoded
    @POST(value = Secret.LOGIN_VALUE)
    fun getNetwork(
        @Field("username") username:String,
        @Field("password") password:String
    ):Call<NetworkUserResponse>
}