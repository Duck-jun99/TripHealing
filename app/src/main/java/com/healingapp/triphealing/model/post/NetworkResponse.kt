package com.healingapp.triphealing.model.post

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/*
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

*/data class NetworkResponse(

    @Expose
    @SerializedName(value = "id")
    val id: String,

    @Expose
    @SerializedName(value = "nickname")
    val nickname: String,

    @Expose
    @SerializedName(value = "title")
    val title: String,

    @Expose
    @SerializedName(value = "text")
    val text: String,

    @Expose
    @SerializedName(value = "created_date")
    val createdDate: String,

    @Expose
    @SerializedName(value = "published_date")
    val publishedDate: String,

    @Expose
    @SerializedName(value = "cover_image")
    val coverImage: String,

    @Expose
    @SerializedName(value = "username")
    val username: String,

    @Expose
    @SerializedName(value = "introduce_text")
    val introduceText: String,

    @Expose
    @SerializedName(value = "profile_img")
    val profileImg: String,

    @Expose
    @SerializedName(value = "description")
    val description: String,

    @Expose
    @SerializedName(value = "views")
    val views: String,

    @Expose
    @SerializedName(value = "comment")
    val comment: List<Comment>,
)

data class Comment(
    @Expose
    @SerializedName(value = "post")
    val post: String,

    @Expose
    @SerializedName(value = "writer")
    val writer: String,

    @Expose
    @SerializedName(value = "body")
    val body: String,

    @Expose
    @SerializedName(value = "date")
    val date: String,

    @Expose
    @SerializedName(value = "writer_img")
    val profileImg: String,

)

data class GetData(
    val getData: List<NetworkResponse>
)