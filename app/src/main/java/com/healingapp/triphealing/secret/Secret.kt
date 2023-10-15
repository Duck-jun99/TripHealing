package com.healingapp.triphealing.secret

object Secret {
    const val BASE_URL = "http://byoungjun320.ddns.net:8000/"

    //MAIN
    const val HOME_URL = BASE_URL
    const val POST_VALUE = "post_json"

    const val MEDIA_URL = BASE_URL + "media/"


    //계정 관련(USER 정보 관련)
    const val USER_URL = HOME_URL + "accounts/home/"

    //LOGIN
    const val LOGIN_VALUE = "app_login"

    //SIGNUP
    const val SIGNUP_VALUE = "app_signup"

    //UPDATE
    const val UPDATE_VALUE = "app_update"
}