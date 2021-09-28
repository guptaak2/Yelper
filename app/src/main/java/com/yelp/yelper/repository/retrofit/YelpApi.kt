package com.yelp.yelper.repository.retrofit

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface YelpApi {

    @GET("v3/businesses/search")
    suspend fun getBusinessByLocation(@Query("location") location: String): Response<YelpResponse>
}