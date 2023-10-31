package com.healingapp.triphealing.network.post

data class ItemFamRV(val title: String, val author: String, val coverImage: String, val view:String)


data class ItemMbtiRV(val title: String, val author: String, val coverImage: String, val text:String)

data class ItemComment(val nickName:String, val comment:String, val date:String, val img:String)