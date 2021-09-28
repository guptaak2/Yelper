package com.yelp.yelper.model

data class Business(
    val businessId: String,
    val name: String,
    val imageUrl: String,
    val city: String,
    val address: String,
    val isClosed: Boolean,
)