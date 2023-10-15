package com.healingapp.triphealing.model.update

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.healingapp.triphealing.model.post.NetworkResponse

data class NetworkUpdateResponse(

    @Expose
    @SerializedName(value = "msg")
    val msg: String,

    @Expose
    @SerializedName(value = "code")
    val code: String,

)


data class GetData(
    val getData: List<NetworkUpdateResponse>
)
