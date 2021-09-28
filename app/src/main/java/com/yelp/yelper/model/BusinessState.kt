package com.yelp.yelper.model

class BusinessState(
    val businesses: List<Business>,
    val error: Error
)

const val NO_ERROR = "NO_ERROR"