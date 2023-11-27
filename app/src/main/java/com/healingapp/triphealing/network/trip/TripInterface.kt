package com.healingapp.triphealing.network.trip

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.healingapp.triphealing.model.post.NetworkCommentResponse
import com.healingapp.triphealing.model.post.NetworkDeleteResponse
import com.healingapp.triphealing.secret.Secret
import com.healingapp.triphealing.model.post.NetworkResponse
import com.healingapp.triphealing.model.trip.NetworkTripDetailInfoResponse
import com.healingapp.triphealing.model.trip.NetworkTripResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface TripInterface {

    @GET(value = Secret.DATA_BASED_AREA)
    fun getNetwork(
        @Query("numOfRows") numOfRows:Int,
        @Query("pageNo") pageNo:Int,
        @Query("MobileOS") MobileOS:String,
        @Query("MobileApp") MobileApp:String,
        @Query("serviceKey") serviceKey:String,
        @Query("_type") _type:String,
        @Query("contentTypeId") contentTypeId:String,
        @Query("areaCode") areaCode:String,
        @Query("sigunguCode") sigunguCode:String,

        ):Call<NetworkTripResponse>

    @GET(value = Secret.DATA_DETAIL)
    fun getDetailNetwork(
        @Query("MobileOS") MobileOS:String,
        @Query("MobileApp") MobileApp:String,
        @Query("serviceKey") serviceKey:String,
        @Query("_type") _type:String,
        @Query("contentId") contentId:String,
        @Query("defaultYN") defaultYN:String,
        @Query("firstImageYN") firstImageYN:String,
        @Query("areacodeYN") areacodeYN:String,
        //@Query("catcodeYN") catcodeYN:String,
        @Query("addrinfoYN") addrinfoYN:String,
        @Query("mapinfoYN") mapinfoYN:String,
        @Query("overviewYN") overviewYN:String,

        ):Call<NetworkTripDetailInfoResponse>

    companion object {
        fun create(): TripInterface {
            val gson: Gson = GsonBuilder().setLenient().create()

            val interceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

            return Retrofit.Builder()
                .baseUrl(Secret.DATA_GO_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(TripInterface::class.java)
        }
    }

}