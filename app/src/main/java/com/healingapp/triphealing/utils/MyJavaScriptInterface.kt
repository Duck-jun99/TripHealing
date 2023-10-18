package com.healingapp.triphealing.utils

import android.content.Context
import android.webkit.JavascriptInterface
import android.widget.Toast

class MyJavaScriptInterface(private val context: Context, token: String) {
    private var token: String = token

    fun setToken(token: String) {
        this.token = token
    }
    @JavascriptInterface
    fun getToken(): String {

        // Android 앱에서 토큰을 가져와서 반환
        return token
    }

    //테스트코드
    @JavascriptInterface
    fun getAndroidVariable(): String {
        // Android 변수나 메서드 반환
        return "Hello from Android"
    }

    //테스트코드 -> 서버에 전달된 token값 보여주기 위함
    @JavascriptInterface
    fun showToast(token: String) {
        Toast.makeText(context, token, Toast.LENGTH_SHORT).show()
    }
}