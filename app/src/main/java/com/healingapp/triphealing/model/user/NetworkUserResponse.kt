package com.healingapp.triphealing.model.user

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.healingapp.triphealing.model.post.NetworkResponse

data class NetworkUserResponse(

    @Expose
    @SerializedName(value = "msg")
    val msg: String,

    @Expose
    @SerializedName(value = "code")
    val code: String,

    @Expose
    @SerializedName(value = "token")
    val token: String,

    @Expose
    @SerializedName(value = "user_info")
    val userInfo: UserInfo

)

data class UserInfo(

    @Expose
    @SerializedName(value = "username")
    val username: String,

    @Expose
    @SerializedName(value = "useremail")
    val author: String,

    @Expose
    @SerializedName(value = "nickname")
    val nickname: String,

    @Expose
    @SerializedName(value = "introduce_text")
    val introduceText: String,

    @Expose
    @SerializedName(value = "profile_img")
    val profileImg: String,

    @Expose
    @SerializedName(value = "background_img")
    val backgroundImg: String,

    @Expose
    @SerializedName(value = "propensity")
    val propensity: Propensity,

    @Expose
    @SerializedName(value = "follower")
    val follower: List<String>,

    @Expose
    @SerializedName(value = "following")
    val following: List<String>,


)

data class Propensity(

    @Expose
    @SerializedName(value = "mbti")
    val mbti: String,

    @Expose
    @SerializedName(value = "option1")
    val option1: String,

    @Expose
    @SerializedName(value = "option2")
    val option2: String,

    @Expose
    @SerializedName(value = "option3")
    val option3: String,

    @Expose
    @SerializedName(value = "option4")
    val option4: String,

    @Expose
    @SerializedName(value = "option5")
    val option5: String,
)

data class SignUpData(

    val option1: String,

    val option2: String,

    val option3: String,

    val option4: String,

    val option5: String,
)

data class UserInfoResponse(

    @Expose
    @SerializedName(value = "username")
    val username: String,

    @Expose
    @SerializedName(value = "useremail")
    val author: String,

    @Expose
    @SerializedName(value = "nickname")
    val nickname: String,

    @Expose
    @SerializedName(value = "propensity")
    val propensity: Propensity,

    @Expose
    @SerializedName(value = "follower")
    val follower: List<String>,

    @Expose
    @SerializedName(value = "following")
    val following: List<String>,

    @Expose
    @SerializedName(value = "loved_post")
    val lovedPost: List<String>,

    @Expose
    @SerializedName(value = "vip_choice")
    val vipChoice: String,

    @Expose
    @SerializedName(value = "profile_img")
    val profileImg: String,

    @Expose
    @SerializedName(value = "background_img")
    val backgroundImg: String,

    @Expose
    @SerializedName(value = "introduce_text")
    val introduceText: String
)

data class GetData(
    val getData: List<NetworkUserResponse>
)
