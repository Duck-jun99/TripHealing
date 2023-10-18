package com.healingapp.triphealing.network.login

import com.healingapp.triphealing.secret.Secret
import com.healingapp.triphealing.model.user.NetworkUserResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface UserChangeInterface {
    @FormUrlEncoded
    @POST(value = Secret.LOGIN_VALUE_CHANGE)
    fun getNetwork(
        @Field("username") username:String,
        @Field("password") password:String
    ):Call<NetworkUserResponse>
}