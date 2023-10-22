package com.healingapp.triphealing.network.post

import com.healingapp.triphealing.secret.Secret
import com.healingapp.triphealing.model.post.NetworkResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers

interface PostFamousInterface {
    @GET(value = Secret.POST_FAMOUS)
    @Headers(
        "accept: application/json",
        "content-type: application/x-www-form-urlencoded; charset=utf-8"
    )
    fun getNetwork():Call<List<NetworkResponse>>

}