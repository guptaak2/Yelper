package com.yelp.yelper.model

sealed class BusinessUiModel {
    class Error(val errorMessage: String) : BusinessUiModel()
    class Businesses(val business: List<Business>) : BusinessUiModel()
}
