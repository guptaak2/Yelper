package com.yelp.yelper.repository

import com.example.yelp_td.R
import com.yelp.yelper.ConnectivityChecker
import com.yelp.yelper.StringResourceProvider
import com.yelp.yelper.model.Business
import com.yelp.yelper.model.BusinessState
import com.yelp.yelper.model.Error
import com.yelp.yelper.model.NO_ERROR
import com.yelp.yelper.repository.retrofit.YelpApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BusinessRepositoryImpl @Inject constructor(
    private val yelpApi: YelpApi,
    private val connectivityChecker: ConnectivityChecker,
    private val stringResourceProvider: StringResourceProvider
) : BusinessRepository {

    override suspend fun getBusinessByLocation(location: String): BusinessState =
        withContext(Dispatchers.IO) {
            if (!connectivityChecker.isConnected()) {
                return@withContext BusinessState(
                    emptyList(),
                    Error(
                        stringResourceProvider.getString(R.string.no_internet_error_code),
                        stringResourceProvider.getString(R.string.no_internet_error_description)
                    )
                )
            }
            return@withContext try {
                val response = yelpApi.getBusinessByLocation(location)
                if (response.isSuccessful) {
                    val body = response.body()
                    val businesses: MutableList<Business> = mutableListOf()
                    if (!body?.businesses.isNullOrEmpty()) {
                        body?.businesses?.map { business ->
                            businesses.add(
                                Business(
                                    business.businessId,
                                    business.name,
                                    business.imageUrl,
                                    business.location?.city.orEmpty(),
                                    business.location?.address1.orEmpty(),
                                    business.isClosed ?: false
                                )
                            )
                        }
                        BusinessState(businesses, Error(NO_ERROR))
                    } else {
                        BusinessState(
                            emptyList(),
                            Error(
                                stringResourceProvider.getString(R.string.no_results_error_code),
                                stringResourceProvider.getString(R.string.no_results_error_description)
                            )
                        )
                    }
                } else {
                    BusinessState(
                        emptyList(),
                        Error(
                            stringResourceProvider.getString(R.string.generic_error_code),
                            stringResourceProvider.getString(R.string.generic_error_description)
                        )
                    )
                }
            } catch (e: Throwable) {
                BusinessState(
                    emptyList(),
                    Error(e.message.toString(), "")
                )
            }
        }
}