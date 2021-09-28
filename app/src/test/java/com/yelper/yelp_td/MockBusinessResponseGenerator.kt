package com.yelper.yelp_td

import com.yelp.yelper.model.Business
import com.yelp.yelper.model.BusinessState
import com.yelp.yelper.model.Error
import com.yelp.yelper.model.NO_ERROR
import com.yelp.yelper.repository.retrofit.BusinessResponse
import com.yelp.yelper.repository.retrofit.Location
import com.yelp.yelper.repository.retrofit.YelpResponse
import java.util.*

object MockBusinessResponseGenerator {
    private const val NAME = "A very real business"
    private const val IMAGE_URL = "http:www.dummy.com"
    private const val CITY = "Toronto"
    private const val ADDRESS = "123 Jan Doe St"

    fun getMockYelpResponse() = YelpResponse(getMockListBusinessResponse())

    fun getBusinessStateError() =
        BusinessState(emptyList(), Error("some error code", "some error message"))

    fun getBusinessStateEmpty() =
        BusinessState(emptyList(), Error(NO_ERROR, ""))

    fun getBusinessStateNoError() = BusinessState(getMockListBusiness(), Error(NO_ERROR, ""))

    private fun getMockBusinessResponse(item: String) = BusinessResponse(
        UUID.randomUUID().toString(),
        "$NAME$item",
        "$IMAGE_URL$item",
        true,
        Location(CITY, ADDRESS)
    )

    private fun getMockListBusiness(): List<Business> = (1..20).map {
        getMockBusiness(it.toString())
    }

    private fun getMockBusiness(item: String) = Business(
        UUID.randomUUID().toString(),
        "$NAME$item",
        "$IMAGE_URL$item",
        CITY,
        ADDRESS,
        true
    )

    private fun getMockListBusinessResponse(): List<BusinessResponse> = (1..20).map {
        getMockBusinessResponse(it.toString())
    }
}
