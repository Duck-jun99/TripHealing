package com.healingapp.triphealing.model.post

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class NetworkResponse(

    @Expose
    @SerializedName(value = "model")
    val model: String,

    @Expose
    @SerializedName(value = "pk")
    val pk: Int,

    @Expose
    @SerializedName(value = "fields")
    val fieldList: FieldModel

)

data class FieldModel(

    @Expose
    @SerializedName(value = "author")
    val author: Int,

    @Expose
    @SerializedName(value = "title")
    val title: String,

    @Expose
    @SerializedName(value = "text")
    val text: String,

    @Expose
    @SerializedName(value = "created_date")
    val created_date: String,

    @Expose
    @SerializedName(value = "published_date")
    val published_date: String,

    @Expose
    @SerializedName(value = "image")
    val image: String
)

data class GetData(
    val getData: List<NetworkResponse>
)