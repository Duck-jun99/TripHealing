package com.healingapp.triphealing.network.post

import com.healingapp.triphealing.model.post.NetworkRecWriterResponse
import com.healingapp.triphealing.secret.Secret
import com.healingapp.triphealing.model.post.NetworkResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface NetworkInterface {
    @GET(value = Secret.POST_VALUE)
    @Headers(
        "accept: application/json",
        "content-type: application/x-www-form-urlencoded; charset=utf-8"
    )
    fun getNetwork():Call<List<NetworkResponse>>
}

interface PostFamousInterface {
    @GET(value = Secret.POST_FAMOUS)
    @Headers(
        "accept: application/json",
        "content-type: application/x-www-form-urlencoded; charset=utf-8"
    )
    fun getNetwork():Call<List<NetworkResponse>>

}

interface PostRecInterface {
    @GET(value = Secret.POST_REC)
    @Headers(
        "accept: application/json",
        "content-type: application/x-www-form-urlencoded; charset=utf-8"
    )
    fun getNetwork():Call<List<NetworkResponse>>

}

interface PostRecWriterInterface {
    @GET(value = Secret.POST_PICK_WRITER)
    @Headers(
        "accept: application/json",
        "content-type: application/x-www-form-urlencoded; charset=utf-8"
    )
    fun getNetwork():Call<List<NetworkRecWriterResponse>>

}
