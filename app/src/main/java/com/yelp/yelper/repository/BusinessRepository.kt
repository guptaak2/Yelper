package com.yelp.yelper.repository

import com.yelp.yelper.model.BusinessState

interface BusinessRepository {

    suspend fun getBusinessByLocation(location: String) : BusinessState
}