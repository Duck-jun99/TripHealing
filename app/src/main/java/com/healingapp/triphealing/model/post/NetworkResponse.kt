package com.healingapp.triphealing.model.post

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
data class NetworkResponse(

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

data class NetworkRecWriterResponse(
    @Expose
    @SerializedName(value = "id")
    val id: String,

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
    @SerializedName(value = "nickname")
    val nickname: String,

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
)

data class NetworkDeleteResponse(
    @Expose
    @SerializedName(value = "code")
    val code: String,

    @Expose
    @SerializedName(value = "msg")
    val msg: String,
)

data class NetworkCommentResponse(
    @Expose
    @SerializedName(value = "code")
    val code: String,

    @Expose
    @SerializedName(value = "msg")
    val msg: String,

    @Expose
    @SerializedName(value = "comment.writer")
    val writer: String,

    @Expose
    @SerializedName(value = "comment.body")
    val body: String,

    @Expose
    @SerializedName(value = "comment.date")
    val date: String,

)