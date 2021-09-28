package com.yelp.yelper.repository.retrofit

import com.google.gson.annotations.SerializedName

class YelpResponse(
    val businesses: List<BusinessResponse>
)

class BusinessResponse(
    @SerializedName("id") val businessId: String,
    @SerializedName("name") val name: String,
    @SerializedName("image_url") val imageUrl: String,
    @SerializedName("is_closed") val isClosed: Boolean?,
    @SerializedName("location") val location: Location?,
)

class Location(
    val city: String?,
    val address1: String?
)
