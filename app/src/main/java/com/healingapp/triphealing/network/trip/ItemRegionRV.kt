package com.healingapp.triphealing.network.trip

import androidx.versionedparcelable.ParcelField

data class ItemRegionRV(
    val region: String,
    val regionImage: Int
)
data class ItemSiGuRV(
    val region: String,
    val regionImage: Int
)


data class ItemTripDetailRV(
    val title:String,
    val addr:String,
    val img:String
)