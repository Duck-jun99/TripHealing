package com.healingapp.triphealing.model.trip

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.healingapp.triphealing.model.post.NetworkResponse

data class NetworkTripResponse(
    val response: ResponseData
)

data class ResponseData(
    val header: Header,
    val body: Body
)

data class Header(
    val resultCode: String,
    val resultMsg: String
)

data class Body(
    val items: Items,
    val numOfRows: Int,
    val pageNo: Int,
    val totalCount: Int
)

data class Items(
    val item: List<Item>
)

data class Item(
    val addr1: String,
    val addr2: String,
    val areacode: String,
    val booktour: String,
    val cat1: String,
    val cat2: String,
    val cat3: String,
    val contentid: String,
    val contenttypeid: String,
    val createdtime: String,
    val firstimage: String,
    val firstimage2: String,
    val cpyrhtDivCd: String,
    val mapx: String,
    val mapy: String,
    val mlevel: String,
    val modifiedtime: String,
    val sigungucode: String,
    val tel: String,
    val title: String,
    val zipcode: String
)

data class NetworkTripDetailInfoResponse(
    val response: ResponseDataDetail
)

data class ResponseDataDetail(
    val header: HeaderDetail,
    val body: BodyDetail
)

data class HeaderDetail(
    val resultCode: String,
    val resultMsg: String
)

data class BodyDetail(
    val items: ItemsDetail,
    val numOfRows: Int,
    val pageNo: Int,
    val totalCount: Int
)

data class ItemsDetail(
    val item: List<ItemDetail>
)

data class ItemDetail(
    val addr1: String,
    val addr2: String,
    val areacode: String,
    val booktour: String,
    val cat1: String,
    val cat2: String,
    val cat3: String,
    val contentid: String,
    val contenttypeid: String,
    val createdtime: String,
    val firstimage: String,
    val firstimage2: String,
    val cpyrhtDivCd: String,
    val mapx: String,
    val mapy: String,
    val mlevel: String,
    val modifiedtime: String,
    val sigungucode: String,
    val tel: String,
    val telname: String,
    val homepage: String,
    val title: String,
    val zipcode: String,
    val overview: String
)
